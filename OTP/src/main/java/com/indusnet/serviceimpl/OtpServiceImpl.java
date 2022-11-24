package com.indusnet.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.indusnet.dto.RequestUserModel;
import com.indusnet.exception.OtpException;
import com.indusnet.model.OtpModel;
import com.indusnet.repository.IOtpRepository;
import com.indusnet.service.IOtpService;
import com.indusnet.util.Util;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This is Implementation class of IOtpService and override all method.
 * and its also Service layer of project.
 *This class have many service logics.
 */

@Service
public class OtpServiceImpl implements IOtpService {

	String otp = null;
	RequestUserModel reqModel = null;
	int resendCount = 0;
	int generateCount = 0;
	int validateCount = 0;
	@Autowired
	private IOtpRepository userRepository;

	/**
	 * This Constructor initialize the userRepository
	 * @param userRepository 
	 */
	public OtpServiceImpl(IOtpRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	/**
	 * this method is generate the otp and 
	 * return otp.
	 */
	
	@Override
	public String generateOtp(RequestUserModel user) throws OtpException {
		generateCount++;
		if(generateCount > 10) 
			throw new OtpException("you exceed the maximum number of attempt.");
		
		reqModel = user;
		String secKey =user.getMobile() + LocalDateTime.now().getNano(); 

		otp = Util.generateTOTP256(secKey,Integer.valueOf(LocalDateTime.now().getNano()) , "6");

		CompletableFuture.delayedExecutor(60, TimeUnit.SECONDS).execute(() -> otp = "" );

		Optional<OtpModel> existedUser = userRepository.findByEmail(user.getEmail());
		existedUser.ifPresentOrElse(x -> {
			OtpModel otpModel = existedUser.get();
			OtpModel newUserModel = OtpModel.builder().id(otpModel.getId()).name(user.getName())
					.email(user.getEmail())
					.mobile(user.getMobile())
					.secret(secKey)
					.build();
			userRepository.save(newUserModel);
		}
		,() -> {
			OtpModel newUserModel = OtpModel.builder().name(user.getName())
					.email(user.getEmail())
					.mobile(user.getMobile())
					.secret(secKey)
					.build();
			userRepository.save(newUserModel);
		});

		return otp;
	}

	/**
	 * This method validate otp and 
	 * return success message if validate successfully or
	 * throw OtpException
	 */
	@Override
	public String validate(String totp) throws OtpException {

		validateCount++;
		if(validateCount > 4) 
			throw new OtpException("you crossed maximum attempt for validation.");
		
		if(Objects.equals(totp, otp)) 
			return "OTP validated successfully";
		
		else if(totp.length() != 6) 
			throw new OtpException("Please enter 6 digit OTP.");
		
		else 
			throw new OtpException("Invalid OTP");
		

	}

	/**
	 * This method resend the otp
	 * if user resend otp 5 times 
	 * and after again resend otp then throws OtpException 
	 */
	@Override
	public String resend() throws OtpException {
		resendCount++;

		if(resendCount > 5) 
			throw new OtpException("your service is block for 24hr");
		

		if(reqModel != null) 
			return generateOtp(reqModel);
		
		else 
			throw new OtpException("User not stored please send User details.");
		

	}

}

package com.indusnet.otp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.indusnet.dto.RequestUserModel;
import com.indusnet.repository.IOtpRepository;
import com.indusnet.service.IOtpService;
import com.indusnet.serviceimpl.OtpServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ValidateOtpTest {

	@Autowired
	IOtpService iUserService;

	@MockBean
	IOtpRepository iUserRepository;
	
	@BeforeEach
	void setUp(){
		this.iUserService = new OtpServiceImpl(this.iUserRepository);
	}
	

	/**
	 * This Test case for check validate or not  
	 */
	@Test
	void validateTest() {
		RequestUserModel otpModel = RequestUserModel.builder()
				.email("arif841236@gmail.com")
				.mobile("9708240059")
				.name("Md Arif")
				.build();
		String otp = iUserService.generateOtp(otpModel);
		String message = iUserService.validate(otp);
		when(message.length() > 0).thenReturn(true);
		assertEquals(6, otp.length());

	}
	

	/**
	 * This Test case for check maximum number of validate otp in one day
	 * if otp generate 4 times then exceed limit error are throws.
	 */
	
	@Test
	void maximumAttemptTest() {
		int count  = 0;
		
		RequestUserModel otpModel = RequestUserModel.builder()
				.email("arif841236@gmail.com")
				.mobile("9708240059")
				.name("Md Arif")
				.build();
		String otp = iUserService.generateOtp(otpModel);
		for(int i = 0; i < 4; i++) {
			count++;
			iUserService.validate(otp);
		}
		
		when(count == 4).thenReturn(true);
		
		assertEquals(4, count);
	}

}

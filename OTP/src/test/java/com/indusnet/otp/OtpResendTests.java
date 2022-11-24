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
class OtpResendTests {
	
	
	
	@Autowired
	IOtpService iUserService;


	@MockBean
	IOtpRepository iUserRepository;

	int count = 0;
	
	@BeforeEach
	void setUp(){
		this.iUserService = new OtpServiceImpl(iUserRepository);
	}
	

	/**
	 * This Test case for check maximum number of resend otp in one day
	 * if otp generate 5 times then exceed limit error are throws.
	 */
	
	@Test
	void resendTest() {
		int count =0;
		RequestUserModel otpModel = RequestUserModel.builder()
				.email("fhjh44374@gmail.com")
				.mobile("8676857467")
				.name("arif")
				.build();
		iUserService.generateOtp(otpModel);
		for(int i=0; i<5; i++) {
			count++;
			iUserService.resend();
		}
		when(count == 5).thenReturn(true);
		
		assertEquals(5, count);
		
	}
}

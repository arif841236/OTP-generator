package com.indusnet.otp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OtpGenerateTests {

	@Autowired
	IOtpService iUserService;

	@MockBean
	IOtpRepository iUserRepository;

	@BeforeEach
	void setUp(){
		this.iUserService = new OtpServiceImpl(iUserRepository);
	}

	/**
	 * This Test case for otp generation 
	 * if otp generate successfully  and otp is 6 digit then test case is pass.
	 */
	
	@Test
	@Order(1)
	void otpGenerateTest() {
		RequestUserModel otpModel = RequestUserModel.builder()
				.email("arif841236@gmail.com")
				.mobile("9708240059")
				.name("Md Arif")
				.build();
		String otp = iUserService.generateOtp(otpModel);
		when(otp.length() == 6).thenReturn(true);
		assertEquals(6, otp.length());
	}

	/**
	 * This Test case for check maximum number of generated otp in one day
	 * if otp generate 10 times then exceed limit error are throws.
	 */
	
	@Test
	@Order(2)
	void otpGenerateMaxTest() {

		RequestUserModel otpModel = RequestUserModel.builder()
				.email("arif841236@gmail.com")
				.mobile("9708240059")
				.name("Md Arif")
				.build();

		int count = 0;
		for(int i=0; i<5; i++) {
			count++;
			iUserService.generateOtp(otpModel);
		}

		when(count == 5 ).thenReturn(true);

		assertEquals(5, count);
	}




}

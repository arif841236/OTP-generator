package com.indusnet;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.indusnet.controller.OtpController;
import com.indusnet.dto.OtpResponseMessage;
import com.indusnet.dto.RequestUserModel;
import com.indusnet.repository.IOtpRepository;
import com.indusnet.service.IOtpService;
import com.indusnet.serviceimpl.OtpServiceImpl;
import com.indusnet.util.ResponeUtil;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class OTPApplicationTests {

	@InjectMocks
	OtpController otpController;
	
	@Autowired
	private IOtpService userService;

	@Autowired
	ResponeUtil responeUtil;
	
	@MockBean
	IOtpRepository iUserRepository;

	/**
	 * This method is call first then call other method.
	 */
	@BeforeEach
	void setUp(){
		this.userService = new OtpServiceImpl(iUserRepository);
		this.otpController = new OtpController(userService,responeUtil);
		
	}

	/**
	 * This Test case for generate otp 
	 * if generate time based otp successfully then test is pass.
	 */
	
	@Test
	@Order(1)
	void otpControllerGenerateTest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        RequestUserModel otpModel = RequestUserModel.builder()
				.email("arif841236@gmail.com")
				.mobile("970824005")
				.name("Md Arif")
				.build();
        ResponseEntity<OtpResponseMessage> responseEntity = otpController.generateOtpHandler(otpModel);
                 assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
      }
	
	/**
	 * This Test case for validationApi 
	 * if validate success fully then test case is pass.
	 */

	@Test
	@Order(2)
	void otpControllerValidateTest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        RequestUserModel otpModel = RequestUserModel.builder()
				.email("arif841236@gmail.com")
				.mobile("970824005")
				.name("Md Arif")
				.build();
        ResponseEntity<OtpResponseMessage> responseEntity = otpController.generateOtpHandler(otpModel);
        ResponseEntity<OtpResponseMessage> responseEntity2 = otpController.validateOtpHandler(responseEntity.getBody().getOtp());
                
        assertThat(responseEntity2.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
      }
	
	/**
	 * This Test case for  resends otp api
	 * if resends successfully and not exceed to number of attempt which is 5 
	 * then test case is pass.
	 */
	
	@Test
	@Order(3)
	void otpControllerResendTest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        otpControllerValidateTest();
        ResponseEntity<OtpResponseMessage> responseEntity2 = otpController.resendOtpHandler();
                
        assertThat(responseEntity2.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
      }

}

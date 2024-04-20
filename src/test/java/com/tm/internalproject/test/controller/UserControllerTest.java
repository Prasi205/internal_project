package com.tm.internalproject.test.controller;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.ResourceAccessException;

import com.tm.internalproject.controller.UserController;
import com.tm.internalproject.pojo.TokenResponsePOJO;
import com.tm.internalproject.pojo.request.LoginRequest;
import com.tm.internalproject.pojo.response.ResponsePojo;
import com.tm.internalproject.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Mock
	private UserService userService;
	
	private UserController userController;
	
	@Test
	void testSigninSuccess() {
		userController = new UserController();
		userService = mock(UserService.class);
		ReflectionTestUtils.setField(userController, "userService", userService);
		
		String email = "prasanna20596@gmail.com";
		String password = "Prasi@205";

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(email);
		loginRequest.setPassword(password);
		
		String uniqueId ="1234";
   		String accessToken = "dummyAccessToken";
   		String refreshToken = "dummyRefreshToken";
   		
   		TokenResponsePOJO tokenResponsePOJO=new TokenResponsePOJO();
		tokenResponsePOJO.setUniqueId(uniqueId);
	    tokenResponsePOJO.setAccessToken(accessToken);
	    tokenResponsePOJO.setRefreshToken(refreshToken);
	
		when(userService.generateToken(loginRequest)).thenReturn(tokenResponsePOJO);
		
		when(userService.checkEmailExistorNot(loginRequest)).thenReturn(true);
		
		when(userService.checkEmailAndPasswordExistOrNot(loginRequest)).thenReturn(true);
		
		ResponseEntity<ResponsePojo> result = userController.signin(loginRequest);
		assertEquals(true, result.getBody().getIsSuccess());
	}
	
	@Test
	void testSigninNullError() {
		userController = new UserController();
		userService = mock(UserService.class);
		ReflectionTestUtils.setField(userController, "userService", userService);
		
		String email = "prasanna20596@gmail.com";
		String password = "Prasi@205";

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(email);
		loginRequest.setPassword(password);
   		
   		TokenResponsePOJO tokenResponsePOJO=null;
	
		when(userService.generateToken(loginRequest)).thenReturn(tokenResponsePOJO);
		
		when(userService.checkEmailExistorNot(loginRequest)).thenReturn(true);
		
		when(userService.checkEmailAndPasswordExistOrNot(loginRequest)).thenReturn(true);
		
		ResponseEntity<ResponsePojo> result = userController.signin(loginRequest);
		
		assertNull(result.getBody().getResponseData());
	}
	
	@Test
	void testSigninRestAPIError() {
       
    	userController = new UserController();
   		userService = mock(UserService.class);
   		ReflectionTestUtils.setField(userController, "userService", userService);

   		String email = "prasanna20596@gmail.com";
   		String password = "Prasi@205";
   		
   		LoginRequest loginRequest = new LoginRequest();
   		loginRequest.setEmail(email);
   		loginRequest.setPassword(password);
   		
   		String uniqueId ="1234";
   		String accessToken = "dummyAccessToken";
   		String refreshToken = "dummyRefreshToken";
   		
   		TokenResponsePOJO tokenResponsePOJO=new TokenResponsePOJO();
		tokenResponsePOJO.setUniqueId(uniqueId);
	    tokenResponsePOJO.setAccessToken(accessToken);
	    tokenResponsePOJO.setRefreshToken(refreshToken);
	    
		when(userService.generateToken(loginRequest))
		      .thenThrow(new ResourceAccessException("Client token generate api is mismatch or cannot connect rest server"))
		      .thenReturn(tokenResponsePOJO);

   		when(userService.checkEmailExistorNot(loginRequest)) .thenReturn(true);

   	    when(userService.checkEmailAndPasswordExistOrNot(loginRequest))  .thenReturn(true);
   		
   		ResponseEntity<ResponsePojo> result= userController.signin(loginRequest);
   		
   		assertNull(result.getBody().getResponseData());
   			
	}
	
	@Test
	void testSigninInvalidEmail(){
		userController = new UserController();
		userService = mock(UserService.class);
		ReflectionTestUtils.setField(userController, "userService", userService);
		
		String email = "prasanna200596@gmail.com";
		String password = "Prasi@205";
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(email);
		loginRequest.setPassword(password);

		when(userService.checkEmailExistorNot(loginRequest)).thenReturn(false);
		
		ResponseEntity<ResponsePojo> result = userController.signin(loginRequest);
		
		assertNotEquals(true, result.getBody().getIsSuccess());
	}
	
	@Test
	void testSigninInvalidPassword(){
		userController = new UserController();
		userService = mock(UserService.class);
		ReflectionTestUtils.setField(userController, "userService", userService);
		
		String email = "prasanna20596@gmail.com";
		String password = "Prasi@200";
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(email);
		loginRequest.setPassword(password);

		when(userService.checkEmailExistorNot(loginRequest)).thenReturn(true);
		
		when(userService.checkEmailAndPasswordExistOrNot(loginRequest)).thenReturn(false);
		
		ResponseEntity<ResponsePojo> result = userController.signin(loginRequest);
		
		assertNotEquals(true, result.getBody().getIsSuccess());
	}
	
	@Test
	void testSigninInvalidDataError() {
       
    	userController = new UserController();
   		userService = mock(UserService.class);
   		ReflectionTestUtils.setField(userController, "userService", userService);

   		String email = "prasanna20596@gmail.com";
   		String password = "Prasi@205";
   		
   		LoginRequest loginRequest = new LoginRequest();
   		loginRequest.setEmail(email);
   		loginRequest.setPassword(password);

   		lenient(). when(userService.checkEmailExistorNot(loginRequest))
   	        .thenThrow(new InvalidDataAccessResourceUsageException("User table cannot be exist"))
   	        .thenReturn(false);

   		lenient(). when(userService.checkEmailAndPasswordExistOrNot(loginRequest))  
   		    .thenThrow(new InvalidDataAccessResourceUsageException("User table cannot be exist"))
	        .thenReturn(false);
   		
   		ResponseEntity<ResponsePojo> result= userController.signin(loginRequest);
   		
   		assertNull(result.getBody().getResponseData());
   			
	}

}

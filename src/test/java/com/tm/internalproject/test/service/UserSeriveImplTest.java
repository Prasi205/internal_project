package com.tm.internalproject.test.service;

import static org.junit.Assert.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tm.internalproject.entity.UserDetails;
import com.tm.internalproject.exception.CustomException;
import com.tm.internalproject.pojo.TokenResponsePOJO;
import com.tm.internalproject.pojo.request.LoginRequest;
import com.tm.internalproject.repository.UserRepository;
import com.tm.internalproject.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserSeriveImplTest {

	@Mock
	private UserRepository userRepository;

	private RestTemplate restTemplate;

	private UserServiceImpl userServiceImpl;

	@Test
	void testGenerateTokenResponse() throws Exception {

		userServiceImpl = new UserServiceImpl();
		userRepository = mock(UserRepository.class);
		restTemplate = mock(RestTemplate.class);

		ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

		Field restTemplateField = UserServiceImpl.class.getDeclaredField("restTemplate");
		restTemplateField.setAccessible(true);
		restTemplateField.set(userServiceImpl, restTemplate);

		String email = "prasanna20596@gmail.com";
		String password = "Prasi@205";

		int userId = 1;
		String name = "Prasanna";
		String uniqueId = "eb06667e-2709-460f-a50e-66753cac54f7";
		Timestamp createdAt = Timestamp.valueOf("2024-02-10 20:36:00.559");
		Timestamp updatedAt = Timestamp.valueOf("2024-04-16 22:44:00.352");

		UserDetails userDetails = new UserDetails();
		userDetails.setUserId(userId);
		userDetails.setName(name);
		userDetails.setEmail(email);
		userDetails.setPassword(password);
		userDetails.setCreatedAt(createdAt);
		userDetails.setUpdatedAt(updatedAt);
		userDetails.setCreatedBy(name);
		userDetails.setUpdatedBy(name);
		userDetails.setUniqueId(uniqueId);

		when(userRepository.findByEmail(email)).thenReturn(userDetails);

		String accessToken = "dummyAccessToken";
		String refreshToken = "dummyRefreshToken";

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode responseBodyNode = objectMapper.createObjectNode();
		ObjectNode responseDataNode = objectMapper.createObjectNode();
		responseDataNode.put("uniqueId", uniqueId);
		responseDataNode.put("accessToken", accessToken);
		responseDataNode.put("refreshToken", refreshToken);
		responseBodyNode.set("responseData", responseDataNode);

		when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(responseBodyNode);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(email);

		TokenResponsePOJO tokenResponsePOJO2 = userServiceImpl.generateToken(loginRequest);

		assertNotNull(tokenResponsePOJO2);
	}

	@Test
	void testgenerateTokenIllegalArgumentError() throws Exception {
		try {
			userServiceImpl = new UserServiceImpl();
			userRepository = mock(UserRepository.class);
			ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

			UserDetails userDetails = new UserDetails();
			userDetails.setUserId(1);
			userDetails.setName("Prasanna");
			userDetails.setEmail("prasanna123@gmail.com");
			userDetails.setPassword("12345");
			userDetails.setCreatedAt(Timestamp.valueOf("2024-02-10 20:36:00.559"));
			userDetails.setUpdatedAt(Timestamp.valueOf("2024-04-16 22:44:00.352"));
			userDetails.setCreatedBy("Prasanna");
			userDetails.setUpdatedBy("Prasanna");
			userDetails.setUniqueId("12345");

			userDetails.getUserId();
			userDetails.getName();
			userDetails.getEmail();
			userDetails.getPassword();
			userDetails.getCreatedAt();
			userDetails.getUpdatedAt();
			userDetails.getCreatedBy();
			userDetails.getUpdatedBy();
			userDetails.getUniqueId();

			when(userRepository.findByEmail(anyString()))
					.thenThrow(new IllegalArgumentException("Given input are invalid data type"))
					.thenReturn(userDetails);

			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setEmail("prasanna123@gmail.com");

			TokenResponsePOJO tokenResponsePOJO = userServiceImpl.generateToken(loginRequest);
			assertNotNull(tokenResponsePOJO);
		} catch (IllegalArgumentException illegalArgumentException) {
			assertEquals("Given input are invalid data type", illegalArgumentException.getMessage());
		}

	}
	
	@Test
	void testgenerateTokenNullError() throws Exception {
		try {
			userServiceImpl = new UserServiceImpl();
			userRepository = mock(UserRepository.class);
			ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

			UserDetails userDetails = new UserDetails();
			userDetails.setUserId(1);
			userDetails.setName("Prasanna");
			userDetails.setEmail("prasanna@gmail.com");
			userDetails.setPassword("12346");
			userDetails.setCreatedAt(Timestamp.valueOf("2024-02-10 20:36:00.559"));
			userDetails.setUpdatedAt(Timestamp.valueOf("2024-04-16 22:44:00.352"));
			userDetails.setCreatedBy("Prasanna");
			userDetails.setUpdatedBy("Prasanna");
			userDetails.setUniqueId("12345");

			userDetails.getUserId();
			userDetails.getName();
			userDetails.getEmail();
			userDetails.getPassword();
			userDetails.getCreatedAt();
			userDetails.getUpdatedAt();
			userDetails.getCreatedBy();
			userDetails.getUpdatedBy();
			userDetails.getUniqueId();

			when(userRepository.findByEmail(anyString())).thenThrow(new NullPointerException("Given input are null"))
					.thenReturn(any());

			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setEmail("prasanna@gmail.com");

			TokenResponsePOJO tokenResponsePOJO = userServiceImpl.generateToken(loginRequest);

			when(userServiceImpl.generateToken(loginRequest)).thenReturn(tokenResponsePOJO);

			assertNotNull(tokenResponsePOJO);

		} catch (NullPointerException nullPointerException) {
			assertEquals("Given input are null", nullPointerException.getMessage());
		}
	}

	@Test
	void testEmailIdisExist() {
		userServiceImpl = new UserServiceImpl();
		userRepository = mock(UserRepository.class);
		ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

		when(userRepository.existsByEmail(anyString())).thenReturn(true);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("prasanna@gmail.com");

		boolean result = userServiceImpl.checkEmailExistorNot(loginRequest);
		assertEquals(true, result);
	}

	@Test
	void testEmailIdTableIstExist() {
		String error = "User table cannot be exist";
		try {
			userServiceImpl = new UserServiceImpl();
			userRepository = mock(UserRepository.class);
			ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

			when(userRepository.existsByEmail(anyString()))
					.thenThrow(new InvalidDataAccessResourceUsageException(error)).thenReturn(false);

			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setEmail("prasanna@gmail.com");

			userServiceImpl.checkEmailExistorNot(loginRequest);

		} catch (Exception e) {
			assertEquals(error, e.getMessage());
		}
	}

	@Test
	void testPassIllegalEmailError() {
		String error = "Email data type is mismatched";
		try {
			userServiceImpl = new UserServiceImpl();
			userRepository = mock(UserRepository.class);
			ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

			when(userRepository.existsByEmail(anyString())).thenThrow(new IllegalArgumentException(error))
					.thenReturn(false);

			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setEmail("prasanna@gmail.com");

			userServiceImpl.checkEmailExistorNot(loginRequest);

		} catch (IllegalArgumentException illegalArgumentException) {
			assertEquals(error, illegalArgumentException.getMessage());
		}
	}

	@Test
	void testEmailIdNullError() {
		String nullError = "Given email is null";

		userServiceImpl = new UserServiceImpl();
		userRepository = mock(UserRepository.class);
		ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

		when(userRepository.existsByEmail(anyString())).thenThrow(new NullPointerException(nullError))
				.thenReturn(false);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("1234");
		loginRequest.setPassword("12345");

		NullPointerException exception = assertThrows(NullPointerException.class, () -> {
			userServiceImpl.checkEmailExistorNot(loginRequest);
		});

		assertEquals(nullError, exception.getMessage());
	}

	@Test
	void testEmailAndPasswrordisExist() {
		userServiceImpl = new UserServiceImpl();
		userRepository = mock(UserRepository.class);
		ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

		when(userRepository.existsByEmailAndPassword(anyString(), anyString())).thenReturn(true);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("prasanna@gmail.com");
		loginRequest.setPassword("1234");

		boolean result = userServiceImpl.checkEmailAndPasswordExistOrNot(loginRequest);
		assertEquals(true, result);
	}

	@Test
	void testEmailIdAndPasswordTableIsExist() {
		String error = "User table cannot be exist";
		try {
			userServiceImpl = new UserServiceImpl();
			userRepository = mock(UserRepository.class);
			ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

			when(userRepository.existsByEmailAndPassword(anyString(), anyString()))
					.thenThrow(new InvalidDataAccessResourceUsageException(error)).thenReturn(false);

			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setEmail("prasanna@gmail.com");
			loginRequest.setPassword("12345");

			userServiceImpl.checkEmailAndPasswordExistOrNot(loginRequest);

		} catch (InvalidDataAccessResourceUsageException invalidDataAccess) {
			assertEquals(error, invalidDataAccess.getMessage());
		}
	}

	@Test
	void testPassIllegalEmailIdAndPasswordError() {
		String error = "Given input data types are mismatched";
		try {
			userServiceImpl = new UserServiceImpl();
			userRepository = mock(UserRepository.class);
			ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

			when(userRepository.existsByEmailAndPassword(anyString(), anyString()))
					.thenThrow(new IllegalArgumentException(error)).thenReturn(false);

			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setEmail("prasanna@gmail.com");
			loginRequest.setPassword("12345");

			userServiceImpl.checkEmailAndPasswordExistOrNot(loginRequest);

		} catch (IllegalArgumentException invalidDataAccess) {
			assertEquals(error, invalidDataAccess.getMessage());
		}
	}

	@Test
	void testEmailIdorPasswordNullError() {
		String nullError = "Given input's are null";
		try {
			userServiceImpl = new UserServiceImpl();
			userRepository = mock(UserRepository.class);
			ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

			when(userRepository.existsByEmailAndPassword(anyString(), anyString()))
					.thenThrow(new NullPointerException(nullError)).thenReturn(false);

			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setEmail("prasanna@gmail.com");
			loginRequest.setPassword("12345");

			userServiceImpl.checkEmailAndPasswordExistOrNot(loginRequest);

		} catch (NullPointerException nullException) {
			assertEquals(nullError, nullException.getMessage());
		}
	}

	@Test
	void testGenerateTokenResponse_InvalidJsonFormat() throws Exception {
		userServiceImpl = new UserServiceImpl();
		userRepository = mock(UserRepository.class);
		restTemplate = mock(RestTemplate.class);

		ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);

		Field restTemplateField = UserServiceImpl.class.getDeclaredField("restTemplate");
		restTemplateField.setAccessible(true);
		restTemplateField.set(userServiceImpl, restTemplate);

		String email = "prasanna20596@gmail.com";
		String password = "Prasi@205";

		int userId = 1;
		String name = "Prasanna";
		String uniqueId = "eb06667e-2709-460f-a50e-66753cac54f7";
		Timestamp createdAt = Timestamp.valueOf("2024-02-10 20:36:00.559");
		Timestamp updatedAt = Timestamp.valueOf("2024-04-16 22:44:00.352");

		UserDetails userDetails = new UserDetails();
		userDetails.setUserId(userId);
		userDetails.setName(name);
		userDetails.setEmail(email);
		userDetails.setPassword(password);
		userDetails.setCreatedAt(createdAt);
		userDetails.setUpdatedAt(updatedAt);
		userDetails.setCreatedBy(name);
		userDetails.setUpdatedBy(name);
		userDetails.setUniqueId(uniqueId);

		when(userRepository.findByEmail(email)).thenReturn(userDetails);

		Object invalidResponse = new Object();
		when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(invalidResponse);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(email);

		assertThrows(CustomException.class, () -> userServiceImpl.generateToken(loginRequest), "Invalid json format structure");
	}

}

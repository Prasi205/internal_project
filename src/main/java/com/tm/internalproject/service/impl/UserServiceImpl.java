package com.tm.internalproject.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.internalproject.entity.UserDetails;
import com.tm.internalproject.exception.CustomException;
import com.tm.internalproject.pojo.TokenPropertiesPOJO;
import com.tm.internalproject.pojo.TokenResponsePOJO;
import com.tm.internalproject.pojo.request.LoginRequest;
import com.tm.internalproject.repository.UserRepository;
import com.tm.internalproject.service.UserService;

/**
 * This class provides the implementation of the UserService interface It
 * contains method to handle the token generation and check given email and
 * password is exist or not
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.accessTokenValidity}")
	private int accessTokenValidity;

	@Value("${jwt.refreshTokenValidity}")
	private int refreshTokenValidity;

	@Autowired
	private UserRepository userRepository;

	private RestTemplate restTemplate;

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * This method is used to generate the token and set to token responsePojo to
	 * return as.
	 * 
	 * @param loginRequest
	 * @return TokenResponsePOJO
	 */
	@Override
	public TokenResponsePOJO generateToken(LoginRequest loginRequest) {
		logger.info("Received the request to generate the access and refresh token");

		try {
			logger.info("Get the details and set to token response pojo");
			TokenResponsePOJO tokenGenerationResponse = new TokenResponsePOJO();

			UserDetails existingEmail = userRepository.findByEmail(loginRequest.getEmail().trim());

			String tokenGenerationUrl = "http://localhost:8083/auth/jwt/generateToken";

			restTemplate = new RestTemplate();

			TokenPropertiesPOJO tokenPropertiesRequest = new TokenPropertiesPOJO();
			tokenPropertiesRequest.setUniqueId(existingEmail.getUniqueId());
			tokenPropertiesRequest.setSecretKey(secretKey);
			tokenPropertiesRequest.setAccessTokenTime(accessTokenValidity);
			tokenPropertiesRequest.setRefreshTokenTime(refreshTokenValidity);

			HttpEntity<TokenPropertiesPOJO> requestEntity = new HttpEntity<>(tokenPropertiesRequest);

			Object object = restTemplate.postForObject(tokenGenerationUrl, requestEntity, Object.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(String.valueOf(objectMapper.writeValueAsString(object)));
			JsonNode responseDataNode = jsonNode.get("responseData");

			String uniqueId = responseDataNode.get("uniqueId").asText();
			String accessToken = responseDataNode.get("accessToken").asText();
			String refreshToken = responseDataNode.get("refreshToken").asText();

			tokenGenerationResponse.setUniqueId(uniqueId);
			tokenGenerationResponse.setAccessToken(accessToken);
			tokenGenerationResponse.setRefreshToken(refreshToken);

			logger.info("Set the access token,refresh token and unique id in TokenResponsePOJO");

			return tokenGenerationResponse;
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("Given input are invalid data type");
			throw new IllegalArgumentException("Given input are invalid data type");
		} catch (NullPointerException nullPointerException) {
			logger.error("Given input are null");
			throw new NullPointerException("Given input are null");
		} catch (JsonProcessingException jsonProcessingException) {
			logger.error("Invalid json format structure");
			throw new CustomException("Invalid json format structure");
		}

	}

	/**
	 * This method is used to check the requested email is exist or not in database
	 * 
	 * @param loginRequest
	 * @return boolean
	 */
	@Override
	public boolean checkEmailExistorNot(LoginRequest loginRequest) {
		logger.info("Received the request to request email is exist or not in database");
		boolean isEmailExist = false;
		try {
			logger.info("Check email is exist or not");
			isEmailExist = userRepository.existsByEmail(loginRequest.getEmail().trim());
		} catch (InvalidDataAccessResourceUsageException invalidDataAccess) {
			logger.error("Table is not exist at database");
			throw new InvalidDataAccessResourceUsageException("User table cannot be exist");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("Email data type is mismatched");
			throw new IllegalArgumentException("Email data type is mismatched");
		} catch (NullPointerException nullPointerException) {
			logger.error("Given email is null");
			throw new NullPointerException("Given email is null");
		}
		logger.info("Return the result as boolean");
		return isEmailExist;
	}

	/**
	 * This method is used to check the requested email and password is exist or not
	 * in database
	 * 
	 * @param loginRequest
	 * @return boolean
	 */
	@Override
	public boolean checkEmailAndPasswordExistOrNot(LoginRequest loginRequest) {
		logger.info("Received the request to request email and password is exist or not in database");
		boolean isEmailAndPasswordExist = false;
		try {
			logger.info("Check email and password is exist or not");
			isEmailAndPasswordExist = userRepository.existsByEmailAndPassword(loginRequest.getEmail().trim(),
					loginRequest.getPassword().trim());
		} catch (InvalidDataAccessResourceUsageException invalidDataAccess) {
			logger.error("Table is not exist at database");
			throw new InvalidDataAccessResourceUsageException("User table cannot be exist");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("Illegal Email id and password");
			throw new IllegalArgumentException("Given input data types are mismatched");
		} catch (NullPointerException nullPointerException) {
			logger.error("Given input's are null");
			throw new NullPointerException("Given input's are null");
		}
		logger.info("Return the result as boolean");
		return isEmailAndPasswordExist;
	}

}

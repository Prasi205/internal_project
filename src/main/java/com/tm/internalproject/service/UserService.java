package com.tm.internalproject.service;

import com.tm.internalproject.pojo.TokenResponsePOJO;
import com.tm.internalproject.pojo.request.LoginRequest;

/**
 * This interface defines the method of token generation and check email and
 * password is exist or not
 * 
 */
public interface UserService {

	/**
	 * This method is used to generate the token and set to token responsePojo to
	 * return as.
	 * 
	 * @param loginRequest
	 * @return TokenResponsePOJO
	 */
	public TokenResponsePOJO generateToken(LoginRequest loginRequest);

	/**
	 * This method is used to check the requested email is exist or not in database
	 * 
	 * @param loginRequest
	 * @return boolean
	 */
	public boolean checkEmailExistorNot(LoginRequest loginRequest);

	/**
	 * This method is used to check the requested email and password is exist or not
	 * in database
	 * 
	 * @param loginRequest
	 * @return boolean
	 */
	public boolean checkEmailAndPasswordExistOrNot(LoginRequest loginRequest);

}

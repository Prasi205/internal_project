package com.tm.internalproject.service;

import com.tm.internalproject.pojo.TokenResponsePOJO;
import com.tm.internalproject.pojo.request.LoginRequest;

public interface UserService {
	
	public TokenResponsePOJO generateToken(LoginRequest loginRequest);

	public boolean checkEmailExistorNot(LoginRequest loginRequest);
	
	public boolean checkEmailAndPasswordExistOrNot(LoginRequest loginRequest);
	
}

package com.tm.internalproject.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tm.internalproject.pojo.TokenResponsePOJO;
import com.tm.internalproject.pojo.request.LoginRequest;
import com.tm.internalproject.pojo.response.ResponsePojo;
import com.tm.internalproject.service.UserService;

/**Controller class handle the sign in, navigate the login and success page
 * 
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * Handles the request to view the Login page.
	 * 
	 * @return Login.jsp
	 */
	@GetMapping("/login")
	public String login() {
		return "Login";
	}

	/**
	 * Handles the generation of access and refresh tokens based on the received
	 * request.
	 * 
	 * @param loginRequest
	 * @return ResponseEntity<ResponsePojo>
	 *
	 */
	@PostMapping("/signin")
	public ResponseEntity<ResponsePojo> signin(@RequestBody @Valid LoginRequest loginRequest) {
		logger.info("Received the request to generate the token");
		ResponsePojo responsePojo = new ResponsePojo();
		try {
			responsePojo = loginCredentialResponse(loginRequest);
			if (Boolean.TRUE.equals(responsePojo.getIsSuccess())) {
				TokenResponsePOJO tokenResponse = userService.generateToken(loginRequest);
				if (tokenResponse!=null) {
					logger.info("Get the tokenResponse and set to responsepojo object");
					responsePojo.response("Token are generated", tokenResponse, true);
				} else {
					logger.info("Get the tokenResponse and set to responsepojo object");
					responsePojo.response("Unable to get the token", null, false);
				}
			} else {
				logger.error("Return the error from responsePojo");
			}
		} catch (Exception e) {
			logger.error("Unable to signin!...");
			responsePojo.response("Unable to signin!...", null, false);
		}
		return ResponseEntity.ok(responsePojo);
	}

	/**
	 * Handles the request to view the success page.
	 * 
	 * @return SuccessPage.jsp
	 */
	@GetMapping("/success")
	public String success() {
		return "SuccessPage";
	}

	/**
	 * Handles the request to validate the login credential to return the response
	 * 
	 * @param loginRequest
	 * @return ResponsePojo
	 */
	private ResponsePojo loginCredentialResponse(LoginRequest loginRequest) {
		logger.info("Received the request to validate the login credential");
		ResponsePojo responsePojo = new ResponsePojo();
		try {
			if (userService.checkEmailExistorNot(loginRequest)) {
				logger.info("Given email is valid email");
				if (userService.checkEmailAndPasswordExistOrNot(loginRequest)) {
					logger.info("Given email is valid email");
					responsePojo.response("Given password is valid", null, true);
				} else {
					logger.error("Please enter the correct password");
					responsePojo.response("Please enter correct password", null, false);
				}
			} else {
				logger.error("Please enter correct email");
				responsePojo.response("Please enter correct email", null, false);
			}
		} catch (Exception e) {
			logger.error("An error occured while validate the login credential");
			responsePojo.response("An error occured while validate the login credential", null, false);
		}
		return responsePojo;
	}

}

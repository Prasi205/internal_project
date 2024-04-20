package com.tm.internalproject.pojo.request;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginRequest {

	@Pattern(regexp = "^\\s*[a-zA-Z0-9][\\w.%+-]*@[a-zA-Z]+\\.[a-zA-Z]{2,}\\s*$", message = "Invalid email format and cannot be blank")
	@Size(min = 10, max = 320, message = "Email should be 10 to 320")
	private String email;

	@Pattern(regexp = "^\\s*(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#@$!%*?^&])[A-Za-z\\d#@$!%*?^&]{8,15}\\s*$", message = "Password needs at least one uppercase letter,at least one lowercase letter,at least one digit,at least one special character from #@$!%*?^&,can’t contain space and 8 to 15 characters")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}

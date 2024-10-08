package com.project.User_Authentication.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRegistrationDTO {
	@NotBlank(message= "email is required")
	@Email(message="email should be valid")
	private String email;
	@NotBlank(message= "password is required")	
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

package com.project.User_Authentication.DTO;

import lombok.Data;

@Data
public class ForgetPasswordDTO {
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}

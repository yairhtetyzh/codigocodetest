package com.demo.codetest.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.demo.codetest.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class UserRegisterDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 5125229837982415028L;

	@NotBlank(message = "User Name must not be empty")
	private String userName;

	@NotBlank(message = "password must not be empty")
	private String password;
	
	@NotBlank(message = "email must not be empty")
    @Email(message = "email must be in a valid format")
	private String email;
	
	public UserRegisterDTO() {

	}

	public UserRegisterDTO(User entity) {
		this.userName = entity.getUserName();
		this.email = entity.getEmail();
	}
}

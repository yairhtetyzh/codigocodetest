package com.demo.codetest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class UserLoginRequestDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 5087272545219072237L;

	@NotBlank(message = "User Name is required.")
	private String userName;

	@NotBlank(message = "Password is required.")
	private String password;
}

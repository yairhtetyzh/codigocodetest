package com.demo.codetest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ChangePasswordReqDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 7928838365153597978L;

	@NotBlank(message = "Old Password is required.")
	private String oldPassword;

	@NotBlank(message = "New Password is required.")
	private String newPassword;
}

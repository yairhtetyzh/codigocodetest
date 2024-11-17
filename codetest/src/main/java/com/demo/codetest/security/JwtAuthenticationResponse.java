package com.demo.codetest.security;

import java.io.Serializable;

import com.demo.codetest.dto.UserRegisterDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 3959441633089113593L;
	private String token;
	private UserRegisterDTO user;
}

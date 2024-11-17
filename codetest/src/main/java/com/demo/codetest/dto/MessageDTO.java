package com.demo.codetest.dto;

import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class MessageDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1294928513766839447L;
	boolean error;
	String message;
	HashMap<String, String> fieldErrorMessages;
}

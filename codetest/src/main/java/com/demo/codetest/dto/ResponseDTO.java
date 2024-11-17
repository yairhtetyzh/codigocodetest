package com.demo.codetest.dto;

import java.io.Serializable;

import com.demo.codetest.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 3211148471148299768L;

	String errorCode;
	
	String message;
	
	T data;
	
	public ResponseDTO(T data) {
		this.errorCode = ErrorCode.ERROR_000000.getCode();
		this.message = ErrorCode.ERROR_000000.getDesc();
		this.data = data;
	}
	
	public ResponseDTO(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
}

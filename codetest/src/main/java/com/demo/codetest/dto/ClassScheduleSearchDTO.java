package com.demo.codetest.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ClassScheduleSearchDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 4232402450700643130L;

	private String name;
	
	private Long countryId;
	
	private Integer page = 1;
}

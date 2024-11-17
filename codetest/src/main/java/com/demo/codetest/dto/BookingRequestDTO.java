package com.demo.codetest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class BookingRequestDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 4774326222503155478L;

	@NotNull(message = "Class Id is required.")
	private Long classScheduleId;

	@NotNull(message = "UserPackage Id is required.")
	private Long userPackageId;
}

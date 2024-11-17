package com.demo.codetest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WaitingListRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1229874072224386929L;

	@NotNull(message = "Class Id is required.")
	private Long classScheduleId;

	@NotNull(message = "UserPackage Id is required.")
	private Long userPackageId;
}

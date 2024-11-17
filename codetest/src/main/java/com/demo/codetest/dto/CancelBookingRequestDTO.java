package com.demo.codetest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CancelBookingRequestDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -7035565673827659176L;

	@NotNull(message = "Booking Id is required.")
	private Long bookingId;
}

package com.demo.codetest.dto;

import java.io.Serializable;

import com.demo.codetest.entity.Booking;
import com.demo.codetest.enums.BookingStatus;
import com.demo.codetest.enums.RefundStatus;
import com.demo.codetest.utils.CommonConstants;
import com.demo.codetest.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class BookingDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 7964847907995317421L;

	private Long id;

	private UserDTO user;

	private ClassScheduleDTO classScheduleDTO;

	private String bookingStatusDesc;

	private Integer creditDeducted;
	
	private Long userPackageId;

	private String bookingTime;
	
	private String cancelTime;
	
	private String refundStatusDesc;
	
	public BookingDTO(Booking entity) {
		this.id = entity.getId();
		this.user = entity.getUser() == null ? null : new UserDTO(entity.getUser());
		this.classScheduleDTO = entity.getClassSchedule() == null ? null : new ClassScheduleDTO(entity.getClassSchedule());
		this.bookingStatusDesc = entity.getBookingStatus() == null ? null : BookingStatus.getDescByCode(entity.getBookingStatus());
		this.creditDeducted = entity.getCreditDeducted();
		this.userPackageId = entity.getUserPackageId();
		this.bookingTime = entity.getBookingTime() == null ? null : CommonUtils.formatLocalDateTime(entity.getBookingTime(), CommonConstants.DATE_FORMAT_yyyymmdd_HHMMSS);
		this.cancelTime = entity.getCancelTime() == null ? null : CommonUtils.formatLocalDateTime(entity.getCancelTime(), CommonConstants.DATE_FORMAT_yyyymmdd_HHMMSS);
		this.refundStatusDesc = entity.getRefundStatus() == null ? null : RefundStatus.getDescByCode(entity.getRefundStatus());
	}
}

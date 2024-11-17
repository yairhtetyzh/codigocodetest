package com.demo.codetest.dto;

import java.io.Serializable;

import com.demo.codetest.entity.WaitingList;
import com.demo.codetest.enums.WaitingListStatus;
import com.demo.codetest.utils.CommonConstants;
import com.demo.codetest.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WaitingListDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 6796843462910538002L;

	private Long id;

	private UserDTO user;

	private ClassScheduleDTO classScheduleDTO;

	private Long userPackageId;

	private Integer creditDeducted;

	private String createdDate;

	private String waitingListStatusDesc;

	public WaitingListDTO(WaitingList entity) {
		this.id = entity.getId();
		this.user = entity.getUser() == null ? null : new UserDTO(entity.getUser());
		this.classScheduleDTO = entity.getClassSchedule() == null ? null
				: new ClassScheduleDTO(entity.getClassSchedule());
		this.userPackageId = entity.getUserPackageId();
		this.creditDeducted = entity.getCreditDeducted();
		this.createdDate = entity.getCreatedDate() == null ? null
				: CommonUtils.formatLocalDateTime(entity.getCreatedDate(), CommonConstants.DATE_FORMAT_yyyymmdd_HHMMSS);
		this.waitingListStatusDesc = entity.getWaitingListStatus() == null ? null
				: WaitingListStatus.getDescByCode(entity.getWaitingListStatus());
	}
}

package com.demo.codetest.dto;

import java.io.Serializable;

import com.demo.codetest.entity.ClassSchedule;
import com.demo.codetest.utils.CommonConstants;
import com.demo.codetest.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ClassScheduleDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -1916427113421180823L;

	private Long id;

	private String name;

	private Long countryId;

	private String countryName;

	private Integer requiredCredit;

	private String startTime;

	private String endTime;

	private Integer totalSlot;

	private Integer availableSlot;
	
	public ClassScheduleDTO(ClassSchedule entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.countryId = entity.getCountry() == null ? null : entity.getCountry().getId();
		this.countryName = entity.getCountry() == null ? null : entity.getCountry().getName();
		this.requiredCredit = entity.getRequiredCredit();
		this.startTime = entity.getStartTime() == null ? null : CommonUtils.formatLocalDateTime(entity.getStartTime(), CommonConstants.DATE_FORMAT_yyyymmdd_HHMMSS);
		this.endTime = entity.getEndTime() == null ? null : CommonUtils.formatLocalDateTime(entity.getEndTime(), CommonConstants.DATE_FORMAT_yyyymmdd_HHMMSS);
	    this.totalSlot = entity.getTotalSlot();
	    this.availableSlot = entity.getAvailableSlot();
	}
}

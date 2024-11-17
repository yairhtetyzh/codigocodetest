package com.demo.codetest.dto;

import java.io.Serializable;

import com.demo.codetest.entity.UserPackage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class UserPackageDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -5757087946730237679L;

	private Long id;

	private Long userId;

	private PackagePlanDTO packagePlanDTO;

	private Integer remainingUserCredit;

	private String userPackageStatus;
	
	private Integer buyCount;

	public UserPackageDTO(UserPackage entity) {
		this.id = entity.getId();
		this.userId = entity.getUser() == null ? null : entity.getUser().getId();
		this.packagePlanDTO = entity.getPackagePlan() == null ? null : new PackagePlanDTO(entity.getPackagePlan());
		this.remainingUserCredit = entity.getRemainingUserCredit();
		this.buyCount = entity.getBuyCount();
	}
}

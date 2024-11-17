package com.demo.codetest.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.demo.codetest.entity.PackagePlan;
import com.demo.codetest.utils.CommonConstants;
import com.demo.codetest.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PackagePlanDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -1224843241453602554L;

	private Long id;

	private String name;

	private Integer credit;

	private BigDecimal price;

	private String expirationDate;

	private Long countryId;

	private String countryName;

	public PackagePlanDTO(PackagePlan entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.credit = entity.getCredit();
		this.price = entity.getPrice();
		this.expirationDate = entity.getExpirationDate() == null ? null
				: CommonUtils.formatLocalDateTime(entity.getExpirationDate(), CommonConstants.DATE_FORMAT_yyyymmdd_HHMMSS);
		this.countryId = entity.getCountry() == null ? null : entity.getCountry().getId();
		this.countryName = entity.getCountry() == null ? null : entity.getCountry().getName();
	}
}

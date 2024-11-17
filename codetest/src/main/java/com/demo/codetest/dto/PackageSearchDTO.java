package com.demo.codetest.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PackageSearchDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 789435776741050593L;

	@Schema(description = "Country ID to filter packages", required = false, example = "null")
	private Long countryId;
	
	@Schema(description = "Page number for pagination", required = false, example = "1")
	Integer page = 1;
}

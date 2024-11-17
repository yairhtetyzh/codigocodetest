package com.demo.codetest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PurchasePackageRequestDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 2005479828700868620L;
	
	@NotNull(message = "PackageId is required.")
    private Long packageId;
    
    private String cardNumber;
    
    private String expireDate;
    
    private String cvc;
}

package com.demo.codetest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PageDTO<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2708699477035905570L;
	List<T> dataList = new ArrayList<>();
    int page;
    int size;
    int numberofElements;
    long totalElements;
    int totalPages;

}

package com.demo.codetest.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass

public abstract class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5185551313363850805L;

	@Column(name = "created_date", columnDefinition = "TIMESTAMP")
	private LocalDateTime createdDate;

	@Column(name = "updated_date", columnDefinition = "TIMESTAMP")
	private LocalDateTime updatedDate;
}

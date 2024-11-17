package com.demo.codetest.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "class_schedule")
public class ClassSchedule extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5798856613470806609L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	@Column(name = "required_credit")
	private Integer requiredCredit;

	@Column(name = "start_time")
	private LocalDateTime startTime;

	@Column(name = "end_time")
	private LocalDateTime endTime;

	@Column(name = "total_slot")
	private Integer totalSlot;
	
	@Column(name = "available_slot")
	private Integer availableSlot;
}

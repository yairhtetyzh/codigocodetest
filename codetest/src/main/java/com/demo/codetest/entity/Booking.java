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
@Table(name = "booking")
public class Booking extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3754258768265726758L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "class_schedule_id")
	private ClassSchedule classSchedule;
	
	@Column(name = "user_package_id")
	private Long userPackageId;

	@Column(name = "booking_status")
	private Integer bookingStatus;

	@Column(name = "credit_deducted")
	private Integer creditDeducted;

	@Column(name = "booking_time")
	private LocalDateTime bookingTime;
	
	@Column(name = "cancel_time")
	private LocalDateTime cancelTime;
	
	@Column(name = "check_in_time")
	private LocalDateTime checkInTime;
	
	@Column(name = "refund_status")
	private Integer refundStatus;
}

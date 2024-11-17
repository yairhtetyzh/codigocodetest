package com.demo.codetest.entity;

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
@Table(name = "waiting_list")
public class WaitingList extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8895733328104431268L;
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
	
	@Column(name = "credit_deducted")
	private Integer creditDeducted;
	
	@Column(name = "waiting_list_status")
	private Integer waitingListStatus;
}

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
@Table(name = "user_package")
public class UserPackage extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7462882558339273284L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "package_id")
	private PackagePlan packagePlan;
	
	@Column(name = "ramaining_user_credit")
	private Integer remainingUserCredit;
	
	@Column(name = "buy_count")
	private Integer buyCount;
}

package com.demo.codetest.entity;

import java.math.BigDecimal;
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
@Table(name = "package")
public class PackagePlan extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5234619856882608516L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "credit")
	private Integer credit;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "expiration_date")
	private LocalDateTime expirationDate;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;
}

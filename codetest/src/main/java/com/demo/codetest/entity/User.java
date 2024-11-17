package com.demo.codetest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.demo.codetest.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8024042163424226784L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_name", unique = true)
	private String userName;

	@Getter(onMethod = @__(@JsonIgnore))
	@Setter
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "is_verified")
	private Boolean isVerified;

	@Column(name = "role_name")
	@Enumerated(EnumType.STRING)
	private UserRole roleName;
}

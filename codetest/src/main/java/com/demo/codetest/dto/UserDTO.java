package com.demo.codetest.dto;

import java.io.Serializable;

import com.demo.codetest.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -7681359140689813323L;
    private Long id;
    private String userName;
    private String email;
    private String roleName;
    
    public UserDTO(User entity) {
    	this.id = entity.getId();
    	this.userName = entity.getUserName();
    	this.email = entity.getEmail();
    	this.roleName = entity.getRoleName().toString();
    }
}

package com.demo.codetest.interf;

import com.demo.codetest.dto.ChangePasswordReqDTO;
import com.demo.codetest.dto.UserDTO;
import com.demo.codetest.dto.UserRegisterDTO;
import com.demo.codetest.entity.User;
import com.demo.codetest.exception.CustomWebServiceException;

public interface IUserService {

	public UserDTO register(UserRegisterDTO userDTO) throws CustomWebServiceException;

	public UserRegisterDTO changePassword(ChangePasswordReqDTO changePasswordReqDTO) throws CustomWebServiceException;

	public User findByUserName(String userName);

	public UserDTO getUserProfile();
}

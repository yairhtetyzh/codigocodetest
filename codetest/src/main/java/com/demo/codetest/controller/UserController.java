package com.demo.codetest.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.codetest.dto.ResponseDTO;
import com.demo.codetest.dto.UserDTO;
import com.demo.codetest.dto.UserRegisterDTO;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.interf.IUserService;

@RestController
@RequestMapping(value = "api")
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserService userService;

	/**
	 * @param user
	 * @param errors
	 * @return
	 * @throws CustomWebServiceException
	 */
	@RequestMapping(value = "user/register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO user) {
		logger.debug("Start Register User.....");
		try {
			UserDTO userDTO = userService.register(user);
			logger.debug("UserName(" + user.getUserName() + ") is end Register......");
			return ResponseEntity.ok(new ResponseDTO<UserDTO>(userDTO));
		} catch (CustomWebServiceException e) {
			logger.error("register CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("register System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
	
	@RequestMapping(value = "auth/userprofile", method = RequestMethod.GET)
	public ResponseEntity<?> getUserProfile() {
		logger.debug("Start get User Profile.....");
		try {
			UserDTO userDTO = userService.getUserProfile();
			logger.debug("getUserProfile End......");
			return ResponseEntity.ok(new ResponseDTO<UserDTO>(userDTO));
		} catch (CustomWebServiceException e) {
			logger.error("getUserProfile CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("getUserProfile System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
}

package com.demo.codetest.controller;

import javax.validation.Valid;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.codetest.dto.ChangePasswordReqDTO;
import com.demo.codetest.dto.MessageDTO;
import com.demo.codetest.dto.ResponseDTO;
import com.demo.codetest.dto.UserLoginRequestDTO;
import com.demo.codetest.dto.UserRegisterDTO;
import com.demo.codetest.entity.User;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.exception.ServiceException;
import com.demo.codetest.interf.IUserService;
import com.demo.codetest.security.JwtAuthenticationResponse;
import com.demo.codetest.security.JwtTokenProvider;

@Transactional
@RestController
public class LoginController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	private IUserService userService;

	/**
	 * @param data
	 * @return
	 * @throws JSONException
	 * @throws CustomWebServiceException
	 */
	@RequestMapping(value = "api/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO)
			throws JSONException, ServiceException {

		User user = userService.findByUserName(userLoginRequestDTO.getUserName());
		// not register
		if (user == null) {
			logger.info("Invalid User");
			MessageDTO message = new MessageDTO();
			message.setError(true);
			message.setMessage("Invalid User");
			return new ResponseEntity<MessageDTO>(message, HttpStatus.UNAUTHORIZED);
		}
		// incorrect password
		if (!passwordEncoder.matches(userLoginRequestDTO.getPassword(), user.getPassword())) {
			logger.info("Incorrect Password.");
			MessageDTO message = new MessageDTO();
			message.setError(true);
			message.setMessage("Incorrect Password");
			return new ResponseEntity<MessageDTO>(message, HttpStatus.UNAUTHORIZED);
		}
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				userLoginRequestDTO.getUserName(), userLoginRequestDTO.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);

		UserRegisterDTO userDTO = new UserRegisterDTO(user);
		JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse(jwt, userDTO);
		return new ResponseEntity<JwtAuthenticationResponse>(jwtResponse, HttpStatus.OK);
	}

	/**
	 * @param data
	 * @return
	 * @throws JSONException
	 * @throws CustomWebServiceException
	 */
	@RequestMapping(value = "api/auth/changepassword", method = RequestMethod.POST)
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordReqDTO changePasswordReqDTO,
			Errors errors) {

		try {
			userService.changePassword(changePasswordReqDTO);
			return ResponseEntity.ok()
					.body(new ResponseDTO<>(ErrorCode.ERROR_000000.getCode(), ErrorCode.ERROR_000000.getDesc()));
		} catch (CustomWebServiceException e) {
			logger.error("changePassword CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("changePassword System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
}

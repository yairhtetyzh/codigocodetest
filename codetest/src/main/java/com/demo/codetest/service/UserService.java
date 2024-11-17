package com.demo.codetest.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.codetest.dto.ChangePasswordReqDTO;
import com.demo.codetest.dto.UserDTO;
import com.demo.codetest.dto.UserRegisterDTO;
import com.demo.codetest.entity.User;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.enums.UserRole;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.interf.IUserService;
import com.demo.codetest.repository.UserRepository;
import com.demo.codetest.security.UserPrincipal;
import com.demo.codetest.utils.CommonUtils;

@Service
public class UserService implements IUserService {

	private final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User findById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public UserDTO register(UserRegisterDTO userDTO) throws CustomWebServiceException {

		User user = new User();
		checkUserNameAlreadyExist(userDTO.getUserName());
		user.setUserName(userDTO.getUserName());
		String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
		user.setPassword(encodedPassword);
		user.setEmail(userDTO.getEmail());
		user.setRoleName(UserRole.NORMAL);
		user.setIsVerified(Boolean.FALSE);
		user.setCreatedDate(LocalDateTime.now());
		user.setUpdatedDate(LocalDateTime.now());
		user = userRepository.save(user);

		boolean emailSent = SendVerifyEmail(userDTO.getEmail());

		if (!emailSent) {
			userRepository.delete(user);
			logger.warn("register user Error Occur cause {}.", ErrorCode.ERROR_000002.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000002.getCode(), ErrorCode.ERROR_000002.getDesc());
		}

		user.setIsVerified(Boolean.TRUE);
		userRepository.save(user);
		UserDTO resp = new UserDTO(user);
		return resp;
	}

	public boolean SendVerifyEmail(String email) {
		return true;
	}

	private void checkUserNameAlreadyExist(String userName) throws CustomWebServiceException {
		Optional<User> userOpt = userRepository.findByUserNameAndIsVerified(userName, Boolean.TRUE);
		if (userOpt.isPresent()) {
			logger.warn("checkUserNameAlreadyExist Error Occur cause {}",
					String.format(ErrorCode.ERROR_000001.getDesc(), userName));
			throw new CustomWebServiceException(ErrorCode.ERROR_000001.getCode(),
					String.format(ErrorCode.ERROR_000001.getDesc(), userName));
		}
	}

	@Override
	public UserRegisterDTO changePassword(ChangePasswordReqDTO changePasswordReqDTO) throws CustomWebServiceException {
		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		Optional<User> userOpt = userRepository.findById(userPrincipal.getId());
		if (userOpt.isPresent()) {
			if (!passwordEncoder.matches(changePasswordReqDTO.getOldPassword(), userOpt.get().getPassword())) {
				logger.warn("changePassword Error occur cause {}.", ErrorCode.ERROR_000003.getDesc());
				throw new CustomWebServiceException(ErrorCode.ERROR_000003.getCode(), ErrorCode.ERROR_000003.getDesc());
			}
			String encodedPassword = passwordEncoder.encode(changePasswordReqDTO.getNewPassword());
			userOpt.get().setPassword(encodedPassword);
			userRepository.save(userOpt.get());
		}
		UserRegisterDTO userDTO = new UserRegisterDTO(userOpt.get());

		return userDTO;
	}

	@Override
	public User findByUserName(String userName) {
		return userRepository.findByUserNameAndIsVerified(userName, Boolean.TRUE).orElse(null);
	}

	@Override
	public UserDTO getUserProfile() {
		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		Optional<User> userOpt = userRepository.findById(userPrincipal.getId());
		
		if(userOpt.isPresent()) {
			UserDTO userDTO = new UserDTO(userOpt.get());
			return userDTO;
		}
		return null;
	}

}

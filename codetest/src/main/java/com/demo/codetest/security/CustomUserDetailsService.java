package com.demo.codetest.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.codetest.entity.User;
import com.demo.codetest.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	protected final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	@Autowired
	UserService userService;

	/**
	 * Load by userName
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// Let people login with either username or email
		User user = userService.findByUserName(userName);
		if (user == null) {
			logger.error("User Not Found");
			throw new UsernameNotFoundException("User Not Found");
		}

		return UserPrincipal.create(user);
	}

	/**
	 * Load by user id
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public UserDetails loadUserById(Long id) {// This method is used by JWTAuthenticationFilter
		User user = userService.findById(id);
		if (user == null) {
			logger.error("User Not Found");
			throw new UsernameNotFoundException("User Not Found");
		}

		return UserPrincipal.create(user);
	}
}

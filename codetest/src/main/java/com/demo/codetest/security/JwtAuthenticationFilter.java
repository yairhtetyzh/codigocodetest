package com.demo.codetest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.codetest.dto.ResponseDTO;
import com.demo.codetest.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);
			if (request.getRequestURI().contains("/auth/")) // validate api for only auth contains url
			{
				if (!StringUtils.hasText(jwt)) {
					// auth key missing
					sendErrorResponse(response, HttpStatus.BAD_REQUEST, ErrorCode.ERROR_000021.getDesc(),
							ErrorCode.ERROR_000021.getCode());
					return;
				}

				Long userId = tokenProvider.getUserIdFromJWT(jwt);
				if (!tokenProvider.validateToken(jwt) || userId == null) {
					// invalid token
					sendErrorResponse(response, HttpStatus.BAD_REQUEST, ErrorCode.ERROR_000022.getDesc(),
							ErrorCode.ERROR_000022.getCode());
					return;
				}

				boolean isexpire = JWTSingleton.getInstance().checkJWTexist(userId, jwt);
				if (isexpire) {
					sendErrorResponse(response, HttpStatus.BAD_REQUEST, ErrorCode.ERROR_000022.getDesc(),
							ErrorCode.ERROR_000022.getCode());
					return;
				}

				UserDetails userDetails = customUserDetailsService.loadUserById(userId);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
			sendErrorResponse(response, HttpStatus.BAD_REQUEST, ErrorCode.ERROR_000022.getDesc(),
					ErrorCode.ERROR_000022.getCode());
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message, String errorCode)
			throws IOException {
		response.setStatus(status.value());
		response.setContentType("application/json");
		ResponseDTO<Object> responseDTO = new ResponseDTO<>(errorCode, message);
		ObjectMapper objectMapper = new ObjectMapper();
		response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
	}

	/**
	 * Get JWT From Request
	 * 
	 * @param request
	 * @return
	 */
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
}
package com.demo.codetest.config;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.demo.codetest.dto.ResponseDTO;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.exception.CustomWebServiceException;

@ControllerAdvice(annotations = { RestController.class })
public class GlobalResponseBodyHandler implements ResponseBodyAdvice<Object> {
	private Logger logger = LoggerFactory.getLogger(GlobalResponseBodyHandler.class);

	@ExceptionHandler(value = CustomWebServiceException.class)
	@ResponseBody
	public Object exceptionHandler(CustomWebServiceException e) {
		String code = e.getErrorCode();
		String msg = e.getErrorMsg();
		if (Strings.isBlank(code)) {
			code = ErrorCode.ERRORCODE_009999.getCode();
			msg = ErrorCode.ERRORCODE_009999.getDesc();
		}

		logger.error("Exception :", e);
		return new ResponseDTO<>(code, msg);
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	@ResponseBody
	public Object exceptionHandler1(AccessDeniedException e) {
		logger.error(":{}", e.getMessage(), e);
		logger.info(" loginName security failure.no permission ......code:[{}],msg:[{}]    ",
				ErrorCode.ERRORCODE_020111.getCode(), ErrorCode.ERRORCODE_020111.getDesc());

		String msg = ErrorCode.ERRORCODE_020111.getDesc();
		String code = ErrorCode.ERRORCODE_020111.getCode();

		return new ResponseDTO<>(code, msg);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errors);
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	@ResponseBody
	public Object exceptionHandler3(BindException e) {
		logger.info("   request params parse fail     the case by ", e);
		String msg = ErrorCode.ERRORCODE_020124.getDesc();
		String code = ErrorCode.ERRORCODE_020124.getCode();

		return new ResponseDTO<>(code, msg);
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Object exceptionHandler4(Exception e) {
		logger.info("   system error     the case by ", e);
		String msg = ErrorCode.ERRORCODE_009999.getDesc();
		String code = ErrorCode.ERRORCODE_009999.getCode();

		return new ResponseDTO<>(code, msg);
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
}
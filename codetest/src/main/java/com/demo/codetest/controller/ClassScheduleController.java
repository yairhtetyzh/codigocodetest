package com.demo.codetest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.codetest.dto.ClassScheduleDTO;
import com.demo.codetest.dto.ClassScheduleSearchDTO;
import com.demo.codetest.dto.PageDTO;
import com.demo.codetest.dto.ResponseDTO;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.interf.IClassScheduleService;

@RestController
@RequestMapping(value = "api/auth")
public class ClassScheduleController {

	private final Logger logger = LoggerFactory.getLogger(ClassScheduleController.class);

	private final IClassScheduleService classScheduleService;

	public ClassScheduleController(IClassScheduleService classScheduleService) {
		this.classScheduleService = classScheduleService;
	}

	@RequestMapping(value = "available-class", method = RequestMethod.POST)
	public ResponseEntity<?> getAvailableClass(@RequestBody ClassScheduleSearchDTO classScheduleSearchDTO) {
		logger.debug("getAvailableClass start.....");
		try {
			PageDTO<ClassScheduleDTO> classSchedulePageDTO = classScheduleService
					.getAvailableClass(classScheduleSearchDTO);
			logger.debug("getAvailableClass end......");
			return ResponseEntity.ok(new ResponseDTO<PageDTO<ClassScheduleDTO>>(classSchedulePageDTO));
		} catch (CustomWebServiceException e) {
			logger.error("getAvailableClass CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("getAvailableClass System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
}

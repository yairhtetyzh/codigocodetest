package com.demo.codetest.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.codetest.dto.ResponseDTO;
import com.demo.codetest.dto.WaitingListDTO;
import com.demo.codetest.dto.WaitingListRequestDTO;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.interf.IWaitingListService;

@RestController
@RequestMapping(value = "api/auth")
public class WaitingListController {

	private final Logger logger = LoggerFactory.getLogger(WaitingListController.class);

	private final IWaitingListService waitingListService;

	public WaitingListController(IWaitingListService waitingListService) {
		this.waitingListService = waitingListService;
	}

	@RequestMapping(value = "add-to-waitlist", method = RequestMethod.POST)
	public ResponseEntity<?> addToWaitList(@Valid @RequestBody WaitingListRequestDTO waitingListRequestDTO) {
		logger.debug("Start addToWaitList .....");
		try {
			WaitingListDTO waitingListDTO = waitingListService.addToWaitList(waitingListRequestDTO);
			logger.debug("End addToWaitList......");
			return ResponseEntity.ok(new ResponseDTO<WaitingListDTO>(waitingListDTO));
		} catch (CustomWebServiceException e) {
			logger.error("addToWaitList CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("addToWaitList System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
}

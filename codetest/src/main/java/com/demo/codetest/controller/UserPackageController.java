package com.demo.codetest.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.codetest.dto.PageDTO;
import com.demo.codetest.dto.PurchasePackageRequestDTO;
import com.demo.codetest.dto.ResponseDTO;
import com.demo.codetest.dto.UserPackageDTO;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.interf.IUserPackageService;

@RestController
@RequestMapping(value = "api/auth")
public class UserPackageController {

	private final Logger logger = LoggerFactory.getLogger(UserPackageController.class);

	private final IUserPackageService userPackageService;

	public UserPackageController(IUserPackageService userPackageService) {
		this.userPackageService = userPackageService;
	}

	@RequestMapping(value = "purchase-package", method = RequestMethod.POST)
	public ResponseEntity<?> purchasePackage(@Valid @RequestBody PurchasePackageRequestDTO purchasePackageRequestDTO) {
		logger.debug("purchasePackage start.....");
		try {
			UserPackageDTO userPackageDTO = userPackageService.purchasePackage(purchasePackageRequestDTO);
			logger.debug("purchasePackage package end......");
			return ResponseEntity.ok(new ResponseDTO<UserPackageDTO>(userPackageDTO));
		} catch (CustomWebServiceException e) {
			logger.error("purchasePackage CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("purchasePackage System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
	
	@RequestMapping(value = "user-package", method = RequestMethod.GET)
	public ResponseEntity<?> getUserPackage(@RequestParam(name = "page", required = false) Integer page) {
		logger.debug("getUserPackage start.....");
		try {
			PageDTO<UserPackageDTO> userPackagePageDTO = userPackageService.getUserPackage(page);
			logger.debug("getUserPackage package end......");
			return ResponseEntity.ok(new ResponseDTO<PageDTO<UserPackageDTO>>(userPackagePageDTO));
		} catch (CustomWebServiceException e) {
			logger.error("getUserPackage CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("getUserPackage System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
}

package com.demo.codetest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.codetest.dto.PackagePlanDTO;
import com.demo.codetest.dto.PackageSearchDTO;
import com.demo.codetest.dto.PageDTO;
import com.demo.codetest.dto.ResponseDTO;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.interf.IPackageService;

@RestController
@RequestMapping(value = "api/auth")
public class PackageController {

	private final Logger logger = LoggerFactory.getLogger(PackageController.class);

	private final IPackageService packageService;

	public PackageController(IPackageService packageService) {
		this.packageService = packageService;
	}

	@RequestMapping(value = "available-packages", method = RequestMethod.POST)
	public ResponseEntity<?> getAvailablePackages(@RequestBody PackageSearchDTO packageSearchDTO) {
		logger.debug("getAvailablePackages start.....");
		try {
			PageDTO<PackagePlanDTO> packagePageDTO = packageService.getAvailablePackages(packageSearchDTO);
			logger.debug("getAvailablePackages package end......");
			return ResponseEntity.ok(new ResponseDTO<PageDTO<PackagePlanDTO>>(packagePageDTO));
		} catch (CustomWebServiceException e) {
			logger.error("getAvailablePackages CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("getAvailablePackages System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
}

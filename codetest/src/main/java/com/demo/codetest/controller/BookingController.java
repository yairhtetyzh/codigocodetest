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

import com.demo.codetest.dto.BookingDTO;
import com.demo.codetest.dto.BookingRequestDTO;
import com.demo.codetest.dto.CancelBookingRequestDTO;
import com.demo.codetest.dto.CheckInBookingRequestDTO;
import com.demo.codetest.dto.ResponseDTO;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.interf.IBookingService;

@RestController
@RequestMapping(value = "api/auth")
public class BookingController {

	private final Logger logger = LoggerFactory.getLogger(BookingController.class);

	private final IBookingService bookingService;

	public BookingController(IBookingService bookingService) {
		this.bookingService = bookingService;
	}

	@RequestMapping(value = "make-booking", method = RequestMethod.POST)
	public ResponseEntity<?> makeBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
		logger.debug("Start Make Booking .....");
		try {
			BookingDTO bookingDTO = bookingService.makeBooking(bookingRequestDTO);
			logger.debug("End Make Booking......");
			return ResponseEntity.ok(new ResponseDTO<BookingDTO>(bookingDTO));
		} catch (CustomWebServiceException e) {
			logger.error("makeBooking CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("makeBooking System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
	
	@RequestMapping(value = "cancel-booking", method = RequestMethod.POST)
	public ResponseEntity<?> cancelBooking(@Valid @RequestBody CancelBookingRequestDTO cancelBookingRequestDTO) {
		logger.debug("Start Cancel Booking .....");
		try {
			BookingDTO bookingDTO = bookingService.cancelBooking(cancelBookingRequestDTO);
			logger.debug("End Cancel Booking......");
			return ResponseEntity.ok(new ResponseDTO<BookingDTO>(bookingDTO));
		} catch (CustomWebServiceException e) {
			logger.error("cancelBooking CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("cancelBooking System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
	
	@RequestMapping(value = "check-in-booking", method = RequestMethod.POST)
	public ResponseEntity<?> checkInBooking(@Valid @RequestBody CheckInBookingRequestDTO checkInBookingRequestDTO) {
		logger.debug("Start Check In Booking .....");
		try {
			BookingDTO bookingDTO = bookingService.checkInBooking(checkInBookingRequestDTO);
			logger.debug("End Check In Booking......");
			return ResponseEntity.ok(new ResponseDTO<BookingDTO>(bookingDTO));
		} catch (CustomWebServiceException e) {
			logger.error("checkInBooking CustomException occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(e.getErrorCode(), e.getErrorMsg()));
		} catch (Exception e) {
			logger.error("checkInBooking System Internal Abnormal occur cause by [{}].", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO<>(ErrorCode.ERROR_999999.getCode(), ErrorCode.ERROR_999999.getDesc()));
		}
	}
}

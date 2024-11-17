package com.demo.codetest.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.demo.codetest.dto.MessageDTO;
import com.demo.codetest.security.UserPrincipal;

public class CommonUtils {
	public static UserPrincipal getUserPrincipalFromAuthentication() {
		UserPrincipal userPrincipal = null;
		Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			Object p = auth.getPrincipal();
			if (p instanceof UserPrincipal) {
				userPrincipal = (UserPrincipal) p;
			}
		}
		return userPrincipal;
	}
	
	public static String localDateToString(LocalDate date, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return date.format(formatter);
    }
	
	public static String formatLocalDateTime(LocalDateTime dateTime, String format) {
        if (dateTime == null || format == null || format.isEmpty()) {
            throw new IllegalArgumentException("DateTime and format must not be null or empty");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }
	
	public static ResponseEntity<?> getFieldErrors(Errors errors) {
		MessageDTO message = new MessageDTO();
		message.setError(true);
		HashMap<String, String> errorMessages = new HashMap<>();
		for (FieldError fieldErr : errors.getFieldErrors()) {
			errorMessages.put(fieldErr.getField(), fieldErr.getDefaultMessage());
		}
		message.setFieldErrorMessages(errorMessages);
		return new ResponseEntity<>(message, HttpStatus.UNPROCESSABLE_ENTITY);
	}
}

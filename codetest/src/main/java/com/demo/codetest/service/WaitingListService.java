package com.demo.codetest.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.codetest.dto.WaitingListDTO;
import com.demo.codetest.dto.WaitingListRequestDTO;
import com.demo.codetest.entity.Booking;
import com.demo.codetest.entity.ClassSchedule;
import com.demo.codetest.entity.User;
import com.demo.codetest.entity.UserPackage;
import com.demo.codetest.entity.WaitingList;
import com.demo.codetest.enums.BookingStatus;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.enums.WaitingListStatus;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.interf.IWaitingListService;
import com.demo.codetest.repository.BookingRepository;
import com.demo.codetest.repository.ClassScheduleRepository;
import com.demo.codetest.repository.UserPackageRepository;
import com.demo.codetest.repository.UserRepository;
import com.demo.codetest.repository.WaitingListRepository;
import com.demo.codetest.security.UserPrincipal;
import com.demo.codetest.utils.CommonUtils;

@Service
public class WaitingListService implements IWaitingListService {

	private final Logger logger = LoggerFactory.getLogger(WaitingListService.class);

	private final WaitingListRepository waitingListRepository;

	private final ClassScheduleRepository classScheduleRepository;

	private final UserPackageRepository userPackageRepository;

	private final UserRepository userRepository;

	private final BookingRepository bookingRepository;

	public WaitingListService(WaitingListRepository waitingListRepository,
			ClassScheduleRepository classScheduleRepository, UserPackageRepository userPackageRepository,
			UserRepository userRepository, BookingRepository bookingRepository) {
		this.waitingListRepository = waitingListRepository;
		this.classScheduleRepository = classScheduleRepository;
		this.userPackageRepository = userPackageRepository;
		this.userRepository = userRepository;
		this.bookingRepository = bookingRepository;
	}

	@Override
	public WaitingListDTO addToWaitList(WaitingListRequestDTO waitingListRequestDTO) {

		ClassSchedule classSchedule = validateClassSchedule(waitingListRequestDTO);
		UserPackage userPackage = validateUserPackage(waitingListRequestDTO);
		User user = checkValidUser();

		checkAlreadyBooking(waitingListRequestDTO);
		checkAlreadyInWaitingList(waitingListRequestDTO);
		if (userPackage.getRemainingUserCredit() < classSchedule.getRequiredCredit()) {
			logger.warn("validateUserPackage Error Occur cause {}", ErrorCode.ERROR_000010.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000010.getCode(), ErrorCode.ERROR_000010.getDesc());
		}

		if (!userPackage.getPackagePlan().getCountry().getId().equals(classSchedule.getCountry().getId())) {
			logger.warn("validateUserPackage Error Occur cause {}", ErrorCode.ERROR_000011.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000011.getCode(), ErrorCode.ERROR_000011.getDesc());
		}

		deductCreditFromUser(userPackage, classSchedule.getRequiredCredit());

		WaitingList waitingList = new WaitingList();
		waitingList.setClassSchedule(classSchedule);
		waitingList.setCreatedDate(LocalDateTime.now());
		waitingList.setUpdatedDate(LocalDateTime.now());
		waitingList.setCreditDeducted(classSchedule.getRequiredCredit());
		waitingList.setUserPackageId(userPackage.getId());
		waitingList.setUser(user);
		waitingList.setWaitingListStatus(WaitingListStatus.WAITING.getCode());

		waitingList = waitingListRepository.save(waitingList);
		WaitingListDTO waitingListDTO = new WaitingListDTO(waitingList);
		return waitingListDTO;
	}

	private void checkAlreadyInWaitingList(WaitingListRequestDTO waitingListRequestDTO) {
		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		Optional<WaitingList> waitingListOpt = waitingListRepository.findByClassScheduleIdAndUserIdAndStatus(
				waitingListRequestDTO.getClassScheduleId(), userPrincipal.getId(), WaitingListStatus.WAITING.getCode());
		if (waitingListOpt.isPresent()) {
			logger.warn("checkAlreadyInWaitingList Error Occur cause {}",
					String.format(ErrorCode.ERROR_000014.getDesc(), waitingListOpt.get().getClassSchedule().getName()));
			throw new CustomWebServiceException(ErrorCode.ERROR_000014.getCode(),
					String.format(ErrorCode.ERROR_000014.getDesc(), waitingListOpt.get().getClassSchedule().getName()));
		}
	}

	private void deductCreditFromUser(UserPackage userPackage, Integer requiredCredit) {
		userPackage.setRemainingUserCredit(userPackage.getRemainingUserCredit() - requiredCredit);
		userPackage.setUpdatedDate(LocalDateTime.now());
		userPackageRepository.save(userPackage);
	}

	private void checkAlreadyBooking(WaitingListRequestDTO waitingListRequestDTO) {
		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		Optional<Booking> bookingOpt = bookingRepository.findByClassScheduleIdAndUserIdAndStatus(
				waitingListRequestDTO.getClassScheduleId(), userPrincipal.getId(), BookingStatus.BOOK.getCode());
		if (bookingOpt.isPresent()) {
			logger.warn("checkAlreadyBooking Error Occur cause {}",
					String.format(ErrorCode.ERROR_000013.getDesc(), bookingOpt.get().getClassSchedule().getName()));
			throw new CustomWebServiceException(ErrorCode.ERROR_000013.getCode(),
					String.format(ErrorCode.ERROR_000013.getDesc(), bookingOpt.get().getClassSchedule().getName()));
		}
	}

	private User checkValidUser() {
		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		User user = userRepository.findById(userPrincipal.getId()).orElse(null);
		return user;
	}

	private UserPackage validateUserPackage(WaitingListRequestDTO waitingListRequestDTO) {
		Optional<UserPackage> userPackageOpt = userPackageRepository.findById(waitingListRequestDTO.getUserPackageId());
		if (!userPackageOpt.isPresent()) {
			logger.warn("validateUserPackage Error Occur cause {}",
					String.format(ErrorCode.ERROR_000004.getDesc(), "UserPackage"));
			throw new CustomWebServiceException(ErrorCode.ERROR_000004.getCode(),
					String.format(ErrorCode.ERROR_000004.getDesc(), "UserPackage"));
		}

		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		if (!userPackageOpt.get().getUser().getId().equals(userPrincipal.getId())) {
			logger.warn("validateUserPackage Error Occur cause {}", ErrorCode.ERROR_000008.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000008.getCode(), ErrorCode.ERROR_000008.getDesc());
		}

		if (userPackageOpt.get().getPackagePlan().getExpirationDate().isBefore(LocalDateTime.now())) {
			logger.warn("validateUserPackage Error Occur cause {}", ErrorCode.ERROR_000005.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000005.getCode(), ErrorCode.ERROR_000005.getDesc());
		}

		if (userPackageOpt.get().getRemainingUserCredit() > 0) {
			return userPackageOpt.get();
		} else {
			logger.warn("validateUserPackage Error Occur cause {}", ErrorCode.ERROR_000009.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000009.getCode(), ErrorCode.ERROR_000009.getDesc());
		}
	}

	private ClassSchedule validateClassSchedule(WaitingListRequestDTO waitingListRequestDTO) {
		Optional<ClassSchedule> classScheduleOpt = classScheduleRepository
				.findById(waitingListRequestDTO.getClassScheduleId());
		if (!classScheduleOpt.isPresent()) {
			logger.warn("validateClassSchedule Error Occur cause {}",
					String.format(ErrorCode.ERROR_000004.getDesc(), "ClassSchedule"));
			throw new CustomWebServiceException(ErrorCode.ERROR_000004.getCode(),
					String.format(ErrorCode.ERROR_000004.getDesc(), "ClassSchedule"));
		}
		if (classScheduleOpt.get().getStartTime().isBefore(LocalDateTime.now())) {
			logger.warn("validateClassSchedule Error Occur cause {}",
					String.format(ErrorCode.ERROR_000006.getDesc(), classScheduleOpt.get().getName()));
			throw new CustomWebServiceException(ErrorCode.ERROR_000006.getCode(),
					String.format(ErrorCode.ERROR_000006.getDesc(), classScheduleOpt.get().getName()));
		}
		if (classScheduleOpt.get().getAvailableSlot() > 0) {
			logger.warn("validateClassSchedule Error Occur cause {}", ErrorCode.ERROR_000023.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000023.getCode(), ErrorCode.ERROR_000023.getDesc());
		}
		return classScheduleOpt.get();
	}

}

package com.demo.codetest.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.codetest.dto.BookingDTO;
import com.demo.codetest.dto.BookingRequestDTO;
import com.demo.codetest.dto.CancelBookingRequestDTO;
import com.demo.codetest.dto.CheckInBookingRequestDTO;
import com.demo.codetest.entity.Booking;
import com.demo.codetest.entity.ClassSchedule;
import com.demo.codetest.entity.User;
import com.demo.codetest.entity.UserPackage;
import com.demo.codetest.entity.WaitingList;
import com.demo.codetest.enums.BookingStatus;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.enums.RefundStatus;
import com.demo.codetest.enums.WaitingListStatus;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.interf.IBookingService;
import com.demo.codetest.repository.BookingRepository;
import com.demo.codetest.repository.ClassScheduleRepository;
import com.demo.codetest.repository.UserPackageRepository;
import com.demo.codetest.repository.UserRepository;
import com.demo.codetest.repository.WaitingListRepository;
import com.demo.codetest.security.UserPrincipal;
import com.demo.codetest.utils.CommonUtils;
import com.demo.codetest.utils.RedisKey;

@Service
public class BookingService implements IBookingService {

	private final Logger logger = LoggerFactory.getLogger(BookingService.class);

	private final BookingRepository bookingRepository;

	private final ClassScheduleRepository classScheduleRepository;

	private final UserPackageRepository userPackageRepository;

	private final UserRepository userRepository;

	private final RedissonClient redissonClient;

	private final WaitingListRepository waitingListRepository;

	public BookingService(RedissonClient redissonClient, BookingRepository bookingRepository,
			ClassScheduleRepository classScheduleRepository, UserPackageRepository userPackageRepository,
			UserRepository userRepository, WaitingListRepository waitingListRepository) {
		this.redissonClient = redissonClient;
		this.bookingRepository = bookingRepository;
		this.classScheduleRepository = classScheduleRepository;
		this.userPackageRepository = userPackageRepository;
		this.userRepository = userRepository;
		this.waitingListRepository = waitingListRepository;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BookingDTO makeBooking(BookingRequestDTO bookingRequestDTO) {
		String lockKey = RedisKey.BOOKING_CLASS_SCHEDULE_KEY + bookingRequestDTO.getClassScheduleId();
		RLock lock = redissonClient.getLock(lockKey);
		try {

			if (!lock.tryLock(5, 10, TimeUnit.SECONDS)) {
				logger.warn("Failed to acquire lock for class schedule {}", bookingRequestDTO.getClassScheduleId());
				throw new CustomWebServiceException(ErrorCode.ERROR_000012.getCode(),
						"Failed to acquire lock. Please try again later.");
			}
			Booking booking = new Booking();

			ClassSchedule classSchedule = validateClassSchedule(bookingRequestDTO);
			UserPackage userPackage = validateUserPackage(bookingRequestDTO);
			User user = checkValidUser();
			checkAlreadyBooking(bookingRequestDTO);
			if (userPackage.getRemainingUserCredit() < classSchedule.getRequiredCredit()) {
				logger.warn("validateUserPackage Error Occur cause {}", ErrorCode.ERROR_000010.getDesc());
				throw new CustomWebServiceException(ErrorCode.ERROR_000010.getCode(), ErrorCode.ERROR_000010.getDesc());
			}

			if (!userPackage.getPackagePlan().getCountry().getId().equals(classSchedule.getCountry().getId())) {
				logger.warn("validateUserPackage Error Occur cause {}", ErrorCode.ERROR_000011.getDesc());
				throw new CustomWebServiceException(ErrorCode.ERROR_000011.getCode(), ErrorCode.ERROR_000011.getDesc());
			}

			deductCreditFromUser(userPackage, classSchedule.getRequiredCredit());

			decreaseClassScheduleAvailableSlot(classSchedule);

			booking.setBookingStatus(BookingStatus.BOOK.getCode());
			booking.setBookingTime(LocalDateTime.now());
			booking.setClassSchedule(classSchedule);
			booking.setCreatedDate(LocalDateTime.now());
			booking.setUpdatedDate(LocalDateTime.now());
			booking.setCreditDeducted(classSchedule.getRequiredCredit());
			booking.setUserPackageId(userPackage.getId());
			booking.setUser(user);

			booking = bookingRepository.save(booking);
			BookingDTO bookingDTO = new BookingDTO(booking);
			return bookingDTO;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.error("Lock acquisition interrupted ", ErrorCode.ERROR_000012.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000012.getCode(), ErrorCode.ERROR_000012.getDesc());
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	private void checkAlreadyBooking(BookingRequestDTO bookingRequestDTO) {
		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		Optional<Booking> bookingOpt = bookingRepository.findByClassScheduleIdAndUserIdAndStatus(
				bookingRequestDTO.getClassScheduleId(), userPrincipal.getId(), BookingStatus.BOOK.getCode());
		if (bookingOpt.isPresent()) {
			logger.warn("checkAlreadyBooking Error Occur cause {}",
					String.format(ErrorCode.ERROR_000013.getDesc(), bookingOpt.get().getClassSchedule().getName()));
			throw new CustomWebServiceException(ErrorCode.ERROR_000013.getCode(),
					String.format(ErrorCode.ERROR_000013.getDesc(), bookingOpt.get().getClassSchedule().getName()));
		}
	}

	private void decreaseClassScheduleAvailableSlot(ClassSchedule classSchedule) {
		classSchedule.setAvailableSlot(classSchedule.getAvailableSlot() - 1);
		classSchedule.setUpdatedDate(LocalDateTime.now());
		classScheduleRepository.save(classSchedule);
	}

	private void deductCreditFromUser(UserPackage userPackage, Integer requiredCredit) {
		userPackage.setRemainingUserCredit(userPackage.getRemainingUserCredit() - requiredCredit);
		userPackage.setUpdatedDate(LocalDateTime.now());
		userPackageRepository.save(userPackage);
	}

	private User checkValidUser() {
		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		User user = userRepository.findById(userPrincipal.getId()).orElse(null);
		return user;
	}

	private ClassSchedule validateClassSchedule(BookingRequestDTO bookingRequestDTO) {
		Optional<ClassSchedule> classScheduleOpt = classScheduleRepository
				.findById(bookingRequestDTO.getClassScheduleId());
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
			return classScheduleOpt.get();
		} else {
			logger.warn("validateClassSchedule Error Occur cause {}",
					String.format(ErrorCode.ERROR_000007.getDesc(), classScheduleOpt.get().getName()));
			throw new CustomWebServiceException(ErrorCode.ERROR_000007.getCode(),
					String.format(ErrorCode.ERROR_000007.getDesc(), classScheduleOpt.get().getName()));
		}

	}

	private UserPackage validateUserPackage(BookingRequestDTO bookingRequestDTO) {
		Optional<UserPackage> userPackageOpt = userPackageRepository.findById(bookingRequestDTO.getUserPackageId());
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

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BookingDTO cancelBooking(CancelBookingRequestDTO cancelBookingRequestDTO) {
		Booking booking = validateCancelBooking(cancelBookingRequestDTO);

		LocalDateTime fourHoursEarlier = booking.getClassSchedule().getStartTime().minusHours(4);
		if (LocalDateTime.now().isBefore(fourHoursEarlier)) {
			refundBooking(booking);
			booking.setRefundStatus(RefundStatus.REFUNDED.getCode());
		} else {
			booking.setRefundStatus(RefundStatus.NOT_REFUNDED.getCode());
		}

		WaitingList waitingList = getWaitingListForBooking(booking);
		if (!Objects.isNull(waitingList)) {
			waitingToBooking(waitingList);
			waitingList.setWaitingListStatus(WaitingListStatus.BOOKED.getCode());
			waitingListRepository.save(waitingList);
		} else {
			ClassSchedule classSchedule = booking.getClassSchedule();
			classSchedule.setAvailableSlot(classSchedule.getAvailableSlot()+1);
		}

		booking.setBookingStatus(BookingStatus.CANCEL.getCode());
		booking.setUpdatedDate(LocalDateTime.now());
		booking.setCancelTime(LocalDateTime.now());
		booking = bookingRepository.save(booking);
		BookingDTO bookingDTO = new BookingDTO(booking);
		return bookingDTO;
	}

	private void waitingToBooking(WaitingList waitingList) {
		Booking booking = new Booking();
		booking.setClassSchedule(waitingList.getClassSchedule());
		booking.setUser(waitingList.getUser());
		booking.setUserPackageId(waitingList.getUserPackageId());
		booking.setBookingStatus(BookingStatus.BOOK.getCode());
		booking.setCreatedDate(LocalDateTime.now());
		booking.setUpdatedDate(LocalDateTime.now());
		booking.setBookingTime(LocalDateTime.now());
		booking.setCreditDeducted(waitingList.getCreditDeducted());
		bookingRepository.save(booking);
	}

	private WaitingList getWaitingListForBooking(Booking booking) {
		Long classScheduleId = booking.getClassSchedule().getId();
		List<WaitingList> waitingLists = waitingListRepository
				.findWaitingList(classScheduleId, WaitingListStatus.WAITING.getCode());
		return waitingLists.isEmpty() ? null : waitingLists.get(0);
	}

	private void refundBooking(Booking booking) {
		Optional<UserPackage> userPackageOpt = userPackageRepository.findById(booking.getUserPackageId());
		if (userPackageOpt.isPresent()) {
			userPackageOpt.get().setRemainingUserCredit(
					userPackageOpt.get().getRemainingUserCredit() + booking.getCreditDeducted());
			userPackageRepository.save(userPackageOpt.get());
		}
	}

	private Booking validateCancelBooking(CancelBookingRequestDTO cancelBookingRequestDTO) {
		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		Optional<Booking> bookingOpt = bookingRepository.findById(cancelBookingRequestDTO.getBookingId());
		if (!bookingOpt.isPresent()) {
			logger.warn("validateCancelBooking Error Occur cause {}",
					String.format(ErrorCode.ERROR_000004.getDesc(), "Booking"));
			throw new CustomWebServiceException(ErrorCode.ERROR_000004.getCode(),
					String.format(ErrorCode.ERROR_000004.getDesc(), "Booking"));
		}
		if (!bookingOpt.get().getUser().getId().equals(userPrincipal.getId())) {
			logger.warn("validateCancelBooking Error Occur cause {}", ErrorCode.ERROR_000017.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000017.getCode(), ErrorCode.ERROR_000017.getDesc());
		}

		if (bookingOpt.get().getBookingStatus().equals(BookingStatus.CANCEL.getCode())) {
			logger.warn("validateCancelBooking Error Occur cause {}", ErrorCode.ERROR_000015.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000015.getCode(), ErrorCode.ERROR_000015.getDesc());
		}

		if (bookingOpt.get().getClassSchedule().getStartTime().isBefore(LocalDateTime.now())) {
			logger.warn("validateCancelBooking Error Occur cause {}", ErrorCode.ERROR_000015.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000015.getCode(), ErrorCode.ERROR_000015.getDesc());
		}
		return bookingOpt.get();
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BookingDTO checkInBooking(CheckInBookingRequestDTO checkInBookingRequestDTO) {
		Booking booking = validateCheckInBooking(checkInBookingRequestDTO.getBookingId());

		booking.setBookingStatus(BookingStatus.CHECKIN.getCode());
		booking.setCheckInTime(LocalDateTime.now());
		booking = bookingRepository.save(booking);
		BookingDTO bookingDTO = new BookingDTO(booking);
		return bookingDTO;
	}

	private Booking validateCheckInBooking(Long bookingId) {
		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
		if (!bookingOpt.isPresent()) {
			logger.warn("validateCheckInBooking Error Occur cause {}",
					String.format(ErrorCode.ERROR_000004.getDesc(), "Booking"));
			throw new CustomWebServiceException(ErrorCode.ERROR_000004.getCode(),
					String.format(ErrorCode.ERROR_000004.getDesc(), "Booking"));
		}
		if (!bookingOpt.get().getUser().getId().equals(userPrincipal.getId())) {
			logger.warn("validateCheckInBooking Error Occur cause {}", ErrorCode.ERROR_000017.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000017.getCode(), ErrorCode.ERROR_000017.getDesc());
		}

		if (!bookingOpt.get().getBookingStatus().equals(BookingStatus.BOOK.getCode())) {
			logger.warn("validateCheckInBooking Error Occur cause {}", String.format(ErrorCode.ERROR_000018.getDesc(),
					BookingStatus.getDescByCode(bookingOpt.get().getBookingStatus())));
			throw new CustomWebServiceException(ErrorCode.ERROR_000018.getCode(),
					String.format(ErrorCode.ERROR_000018.getDesc(),
							BookingStatus.getDescByCode(bookingOpt.get().getBookingStatus())));
		}

		if (LocalDateTime.now().isBefore(bookingOpt.get().getClassSchedule().getStartTime())) {
			logger.warn("validateCheckInBooking Error Occur cause {}", ErrorCode.ERROR_000019.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000019.getCode(), ErrorCode.ERROR_000019.getDesc());
		}

		if (LocalDateTime.now().isAfter(bookingOpt.get().getClassSchedule().getEndTime())) {
			logger.warn("validateCheckInBooking Error Occur cause {}", ErrorCode.ERROR_000020.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000020.getCode(), ErrorCode.ERROR_000020.getDesc());
		}

		return bookingOpt.get();
	}

}

package com.demo.codetest.jobs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.demo.codetest.entity.ClassSchedule;
import com.demo.codetest.entity.UserPackage;
import com.demo.codetest.entity.WaitingList;
import com.demo.codetest.enums.WaitingListStatus;
import com.demo.codetest.repository.ClassScheduleRepository;
import com.demo.codetest.repository.UserPackageRepository;
import com.demo.codetest.repository.WaitingListRepository;

@Component
public class WaitingListJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(WaitingListJob.class);

	@Autowired
	private WaitingListRepository waitingListRepository;

	@Autowired
	private UserPackageRepository userPackageRepository;

	@Autowired
	private ClassScheduleRepository classScheduleRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Starting WaitingListJob execution at {}", LocalDateTime.now());
		try {
			List<WaitingList> waitingLists = waitingListRepository.findClassEndWaitingList(LocalDateTime.now());
			if (!CollectionUtils.isEmpty(waitingLists)) {
				for (WaitingList waitingList : waitingLists) {
					try {
						refundWaitingList(waitingList);
					} catch (Exception e) {
						logger.error("WaitingListJob : Failed to process waiting list ID {}: {}", waitingList.getId(), e.getMessage(),
								e);
					}
				}
			} else {
				logger.info("WaitingListJob : No waiting list entries found to process.");
			}
		} catch (Exception e) {
			logger.error("WaitingListJob : Error occurred during WaitingListJob execution: {}", e.getMessage(), e);
			throw new JobExecutionException(e);
		}
		logger.info("WaitingListJob : Completed WaitingListJob execution.");
	}

	@Transactional
	private void refundWaitingList(WaitingList waitingList) {
		logger.info("refundWaitingList Refund Processing waiting list ID {}", waitingList.getId());

		// Optional<User> userOpt =
		// userRepository.findById(waitingList.getUser().getId());
		Optional<UserPackage> userPackageOpt = userPackageRepository.findById(waitingList.getUserPackageId());
		Optional<ClassSchedule> classScheduleOpt = classScheduleRepository
				.findById(waitingList.getClassSchedule().getId());

		if (userPackageOpt.isEmpty() || classScheduleOpt.isEmpty()) {
			logger.error("refundWaitingList : UserPackage or ClassSchedule not found for waiting list ID {}", waitingList.getId());
			return;
		}

		ClassSchedule classSchedule = classScheduleOpt.get();
		UserPackage userPackage = userPackageOpt.get();

		// Double check if the class has ended and the waiting list status is valid
		if (classSchedule.getEndTime().isBefore(LocalDateTime.now())
				&& waitingList.getWaitingListStatus().equals(WaitingListStatus.WAITING.getCode())) {
			try {
				userPackage
						.setRemainingUserCredit(userPackage.getRemainingUserCredit() + waitingList.getCreditDeducted());
				userPackageRepository.save(userPackage);

				waitingList.setWaitingListStatus(WaitingListStatus.REFUNDED.getCode());
				waitingListRepository.save(waitingList);

				logger.info("refundWaitingList : Successfully refunded waiting list ID {}", waitingList.getId());
			} catch (Exception e) {
				logger.error("refundWaitingList : Failed to refund waiting list ID {}: {}", waitingList.getId(), e.getMessage(), e);
				throw e; 
			}
		} else {
			logger.warn("refundWaitingList : Skipping waiting list ID {} as it does not meet refund criteria.", waitingList.getId());
		}
	}

}

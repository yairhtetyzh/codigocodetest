package com.demo.codetest.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.demo.codetest.dto.PageDTO;
import com.demo.codetest.dto.PurchasePackageRequestDTO;
import com.demo.codetest.dto.UserPackageDTO;
import com.demo.codetest.entity.PackagePlan;
import com.demo.codetest.entity.User;
import com.demo.codetest.entity.UserPackage;
import com.demo.codetest.enums.ErrorCode;
import com.demo.codetest.enums.UserPackageStatus;
import com.demo.codetest.exception.CustomWebServiceException;
import com.demo.codetest.interf.IUserPackageService;
import com.demo.codetest.repository.PackageRepository;
import com.demo.codetest.repository.UserPackageRepository;
import com.demo.codetest.repository.UserRepository;
import com.demo.codetest.security.UserPrincipal;
import com.demo.codetest.utils.CommonConstants;
import com.demo.codetest.utils.CommonUtils;

@Service
public class UserPackageService implements IUserPackageService {

	private final Logger logger = LoggerFactory.getLogger(UserPackageService.class);

	private final UserPackageRepository userPackageRepository;

	private final UserRepository userRepository;

	private final PackageRepository packageRepository;

	public UserPackageService(UserPackageRepository userPackageRepository, UserRepository userRepository,
			PackageRepository packageRepository) {
		this.userPackageRepository = userPackageRepository;
		this.userRepository = userRepository;
		this.packageRepository = packageRepository;
	}

	@Override
	public UserPackageDTO purchasePackage(PurchasePackageRequestDTO purchasePackageRequestDTO) {

		PackagePlan packagePlan = validatePackage(purchasePackageRequestDTO.getPackageId());

		makePayment(purchasePackageRequestDTO, packagePlan);

		UserPrincipal userPrincipal = CommonUtils.getUserPrincipalFromAuthentication();
		UserPackage userPackage = userPackageRepository.findByUserIdAndPackagePlanId(userPrincipal.getId(),
				purchasePackageRequestDTO.getPackageId());
		if (Objects.isNull(userPackage)) {
			userPackage = new UserPackage();
			userPackage.setBuyCount(1);
			Optional<User> userOpt = userRepository.findById(userPrincipal.getId());
			userPackage.setUser(userOpt.get());
			userPackage.setPackagePlan(packagePlan);
			userPackage.setRemainingUserCredit(packagePlan.getCredit());
			userPackage.setCreatedDate(LocalDateTime.now());
			userPackage.setUpdatedDate(LocalDateTime.now());
		} else {
			userPackage.setBuyCount(userPackage.getBuyCount() + 1);
			userPackage.setRemainingUserCredit(userPackage.getRemainingUserCredit() + packagePlan.getCredit());
			userPackage.setUpdatedDate(LocalDateTime.now());
		}
		userPackage = userPackageRepository.save(userPackage);
		UserPackageDTO userPackageDTO = new UserPackageDTO(userPackage);
		return userPackageDTO;
	}

	private void makePayment(PurchasePackageRequestDTO purchasePackageRequestDTO, PackagePlan packagePlan) {
		boolean cardAdded = AddPaymentCard(purchasePackageRequestDTO.getCardNumber(),
				purchasePackageRequestDTO.getExpireDate(), purchasePackageRequestDTO.getCvc());
		if (cardAdded) {
			boolean paymentSuccessful = PaymentCharge(purchasePackageRequestDTO.getCardNumber(),
					packagePlan.getPrice());
			if (paymentSuccessful) {
				return;
			} else {
				logger.warn("makePayment Payment fail.");
			}
		} else {
			logger.warn("Fail to Add card.");
		}
	}

	public boolean AddPaymentCard(String cardNumber, String expiryDate, String cvv) {
		return true;
	}

	public boolean PaymentCharge(String cardNumber, BigDecimal amount) {
		return true;
	}

	private PackagePlan validatePackage(Long packageId) {
		Optional<PackagePlan> packagePlanOpt = packageRepository.findById(packageId);
		if (!packagePlanOpt.isPresent()) {
			logger.warn("validatePackage Error Occur cause {}",
					String.format(ErrorCode.ERROR_000004.getDesc(), "Package"));
			throw new CustomWebServiceException(ErrorCode.ERROR_000004.getCode(),
					String.format(ErrorCode.ERROR_000004.getDesc(), "Package"));
		}
		boolean isExpire = isPackageExpired(packagePlanOpt.get().getExpirationDate());
		if (isExpire) {
			logger.warn("validatePackage Error Occur cause {}", ErrorCode.ERROR_000005.getDesc());
			throw new CustomWebServiceException(ErrorCode.ERROR_000005.getCode(), ErrorCode.ERROR_000005.getDesc());
		}
		return packagePlanOpt.get();
	}

	public boolean isPackageExpired(LocalDateTime expirationDate) {
		LocalDateTime today = LocalDateTime.now();
		return expirationDate != null && expirationDate.isBefore(today);
	}

	@Override
	public PageDTO<UserPackageDTO> getUserPackage(Integer page) {
		PageDTO<UserPackageDTO> pageDTO = new PageDTO<>();
		if (Objects.isNull(page))
			page = 1;
		Sort sort = Sort.by(Sort.Direction.DESC, "updated_date");
		Pageable pageable = PageRequest.of(page - 1, CommonConstants.DEFAULT_SIZE, sort);

		Page<UserPackage> userPackagePage = userPackageRepository
				.findByUserId(CommonUtils.getUserPrincipalFromAuthentication().getId(), pageable);
		List<UserPackage> userPackageList = userPackagePage.getContent();
		List<UserPackageDTO> userPackageDTOList = new ArrayList<>();
		for (UserPackage up : userPackageList) {
			UserPackageDTO upDTO = new UserPackageDTO(up);
			if (up.getPackagePlan().getExpirationDate().isBefore(LocalDateTime.now())) {
				upDTO.setUserPackageStatus(UserPackageStatus.EXPIRED.getDesc());
			} else
				upDTO.setUserPackageStatus(UserPackageStatus.AVAILABLE.getDesc());
			userPackageDTOList.add(upDTO);
		}

		pageDTO.setDataList(userPackageDTOList);
		pageDTO.setNumberofElements(userPackagePage.getNumberOfElements());
		pageDTO.setPage(userPackagePage.getNumber());
		pageDTO.setSize(userPackagePage.getSize());
		pageDTO.setTotalElements(userPackagePage.getTotalElements());
		pageDTO.setTotalPages(userPackagePage.getTotalPages());
		return pageDTO;
	}

}

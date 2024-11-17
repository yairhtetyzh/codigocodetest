package com.demo.codetest.interf;

import com.demo.codetest.dto.PageDTO;
import com.demo.codetest.dto.PurchasePackageRequestDTO;
import com.demo.codetest.dto.UserPackageDTO;

public interface IUserPackageService {

	UserPackageDTO purchasePackage(PurchasePackageRequestDTO purchasePackageRequestDTO);

	PageDTO<UserPackageDTO> getUserPackage(Integer page);

}

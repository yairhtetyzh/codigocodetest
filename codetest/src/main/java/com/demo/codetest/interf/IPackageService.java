package com.demo.codetest.interf;

import com.demo.codetest.dto.PackagePlanDTO;
import com.demo.codetest.dto.PackageSearchDTO;
import com.demo.codetest.dto.PageDTO;

public interface IPackageService {

	PageDTO<PackagePlanDTO> getAvailablePackages(PackageSearchDTO packageSearchDTO);
}

package com.demo.codetest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.demo.codetest.dto.PackagePlanDTO;
import com.demo.codetest.dto.PackageSearchDTO;
import com.demo.codetest.dto.PageDTO;
import com.demo.codetest.entity.PackagePlan;
import com.demo.codetest.interf.IPackageService;
import com.demo.codetest.repository.PackageRepository;
import com.demo.codetest.spec.PackageSpecification;
import com.demo.codetest.utils.CommonConstants;

@Service
public class PackageService implements IPackageService {

	//private final Logger logger = LoggerFactory.getLogger(PackageService.class);

	private PackageRepository packageRepository;

	public PackageService(PackageRepository packageRepository) {
		this.packageRepository = packageRepository;
	}

	@Override
	public PageDTO<PackagePlanDTO> getAvailablePackages(PackageSearchDTO packageSearchDTO) {
		PageDTO<PackagePlanDTO> pageDTO = new PageDTO<>();
		if (packageSearchDTO.getPage() == null)
			packageSearchDTO.setPage(1);
		Sort sort = Sort.by(Sort.Direction.DESC, "updatedDate");
		Pageable pageable = PageRequest.of(packageSearchDTO.getPage() - 1, CommonConstants.DEFAULT_SIZE, sort);
		Specification<PackagePlan> packageSpec = PackageSpecification.getAvailablePackages(packageSearchDTO);
		Page<PackagePlan> page = packageRepository.findAll(packageSpec, pageable);
		List<PackagePlan> packageList = page.getContent();
		List<PackagePlanDTO> packageDTOList = new ArrayList<>();
		for (PackagePlan pk : packageList) {
			PackagePlanDTO pkDTO = new PackagePlanDTO(pk);
			packageDTOList.add(pkDTO);
		}

		pageDTO.setDataList(packageDTOList);
		pageDTO.setNumberofElements(page.getNumberOfElements());
		pageDTO.setPage(page.getNumber());
		pageDTO.setSize(page.getSize());
		pageDTO.setTotalElements(page.getTotalElements());
		pageDTO.setTotalPages(page.getTotalPages());
		return pageDTO;
	}

}

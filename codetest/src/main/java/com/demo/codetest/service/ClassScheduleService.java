package com.demo.codetest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.demo.codetest.dto.ClassScheduleDTO;
import com.demo.codetest.dto.ClassScheduleSearchDTO;
import com.demo.codetest.dto.PageDTO;
import com.demo.codetest.entity.ClassSchedule;
import com.demo.codetest.interf.IClassScheduleService;
import com.demo.codetest.repository.ClassScheduleRepository;
import com.demo.codetest.spec.ClassScheduleSpecification;
import com.demo.codetest.utils.CommonConstants;

@Service
public class ClassScheduleService implements IClassScheduleService {

	private final ClassScheduleRepository classScheduleRepository;

	public ClassScheduleService(ClassScheduleRepository classScheduleRepository) {
		this.classScheduleRepository = classScheduleRepository;
	}

	@Override
	public PageDTO<ClassScheduleDTO> getAvailableClass(ClassScheduleSearchDTO classSechduleSearchDTO) {
		PageDTO<ClassScheduleDTO> pageDTO = new PageDTO<>();
		if (classSechduleSearchDTO.getPage() == null)
			classSechduleSearchDTO.setPage(1);
		Sort sort = Sort.by(Sort.Direction.DESC, "updatedDate");
		Pageable pageable = PageRequest.of(classSechduleSearchDTO.getPage() - 1, CommonConstants.DEFAULT_SIZE, sort);
		Specification<ClassSchedule> classScheduleSpec = ClassScheduleSpecification
				.getAvailabeClassSchedules(classSechduleSearchDTO);
		Page<ClassSchedule> page = classScheduleRepository.findAll(classScheduleSpec, pageable);
		List<ClassSchedule> classScheduleList = page.getContent();
		List<ClassScheduleDTO> classScheduleDTOList = new ArrayList<>();
		for (ClassSchedule cs : classScheduleList) {
			ClassScheduleDTO csDTO = new ClassScheduleDTO(cs);
			classScheduleDTOList.add(csDTO);
		}

		pageDTO.setDataList(classScheduleDTOList);
		pageDTO.setNumberofElements(page.getNumberOfElements());
		pageDTO.setPage(page.getNumber());
		pageDTO.setSize(page.getSize());
		pageDTO.setTotalElements(page.getTotalElements());
		pageDTO.setTotalPages(page.getTotalPages());
		return pageDTO;
	}
}

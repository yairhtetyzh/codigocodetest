package com.demo.codetest.interf;

import com.demo.codetest.dto.ClassScheduleDTO;
import com.demo.codetest.dto.ClassScheduleSearchDTO;
import com.demo.codetest.dto.PageDTO;

public interface IClassScheduleService {

	PageDTO<ClassScheduleDTO> getAvailableClass(ClassScheduleSearchDTO classSechduleSearchDTO);
}

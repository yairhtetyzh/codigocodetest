package com.demo.codetest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.demo.codetest.entity.ClassSchedule;

@Repository
public interface ClassScheduleRepository
		extends JpaRepository<ClassSchedule, Long>, JpaSpecificationExecutor<ClassSchedule> {

}

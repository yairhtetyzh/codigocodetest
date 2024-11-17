package com.demo.codetest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.demo.codetest.entity.PackagePlan;

@Repository
public interface PackageRepository extends JpaRepository<PackagePlan, Long>, JpaSpecificationExecutor<PackagePlan> {

}

package com.demo.codetest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.codetest.entity.UserPackage;

@Repository
public interface UserPackageRepository extends JpaRepository<UserPackage, Long>, JpaSpecificationExecutor<UserPackage> {

	@Query(value = "Select * From user_package up "
			+ "WHERE up.user_id = :userId "
			+ "And up.package_id = :packageId "
			, nativeQuery = true)
	UserPackage findByUserIdAndPackagePlanId(@Param("userId")Long userId, @Param("packageId") Long packageId);

	@Query(value = "Select * From user_package up "
			+ "WHERE up.user_id = :userId "
			, nativeQuery = true)
	Page<UserPackage> findByUserId(@Param("userId") Long userId, Pageable pageable);

}

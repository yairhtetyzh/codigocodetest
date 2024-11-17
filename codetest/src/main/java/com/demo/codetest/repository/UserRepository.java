package com.demo.codetest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.codetest.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	@Query(value = "SELECT * FROM user u "
			+ "WHERE u.user_name=:userName "
			+ "AND u.is_verified=:verified "
			, nativeQuery = true)
	Optional<User> findByUserNameAndIsVerified(@Param("userName")String userName, @Param("verified") Boolean verified);
}

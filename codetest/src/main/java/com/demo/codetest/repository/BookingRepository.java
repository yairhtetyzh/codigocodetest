package com.demo.codetest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.codetest.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

	@Query( value = "Select * From booking b "
			+ "WHERE (b.class_schedule_id=:classScheduleId) "
			+ "AND (b.user_id=:userId) "
			+ "AND (b.booking_status = :bookingStatus) "
			, nativeQuery = true)
	Optional<Booking> findByClassScheduleIdAndUserIdAndStatus(@Param("classScheduleId") Long classScheduleId,
			@Param("userId") Long userId, @Param("bookingStatus") Integer bookingStatus);

}

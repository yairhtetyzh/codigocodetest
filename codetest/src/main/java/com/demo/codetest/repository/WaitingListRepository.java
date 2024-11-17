package com.demo.codetest.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.codetest.entity.WaitingList;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Long>, JpaSpecificationExecutor<WaitingList> {

	@Query( value = "Select * FROM waiting_list w "
			+ "WHERE w.class_schedule_id=:classScheduleId "
			+ "AND w.user_id =:userId "
			+ "AND w.waiting_list_status =:status "
			, nativeQuery = true)
	Optional<WaitingList> findByClassScheduleIdAndUserIdAndStatus(@Param("classScheduleId") Long classScheduleId,
			@Param("userId") Long userId, @Param("status") Integer status);

	
	@Query(value = "SELECT w FROM WaitingList w "
			+ "WHERE (w.classSchedule.id = :classScheduleId) "
			+ "AND (w.waitingListStatus = :waitingListStatus) "
			+ "ORDER BY w.createdDate ASC ")
	List<WaitingList> findWaitingList(@Param("classScheduleId") Long classScheduleId,
			@Param("waitingListStatus") Integer waitingListStatus);


	@Query(value = "SELECT w.* FROM waiting_list w "
			+ "JOIN class_schedule cs ON cs.id = w.class_schedule_id "
			+ "WHERE (w.waiting_list_status = 1) "
			+ "AND (cs.end_time < :currentTime) "
			, nativeQuery = true)
	List<WaitingList> findClassEndWaitingList(@Param("currentTime") LocalDateTime currentTime);

}

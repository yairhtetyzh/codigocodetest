package com.demo.codetest.spec;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.demo.codetest.dto.ClassScheduleSearchDTO;
import com.demo.codetest.entity.ClassSchedule;
import com.demo.codetest.entity.Country;

public class ClassScheduleSpecification {

	public static Specification<ClassSchedule> getAvailabeClassSchedules(ClassScheduleSearchDTO classScheduleSearchDTO) {

		return new Specification<ClassSchedule>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ClassSchedule> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				final Collection<Predicate> predicates = new ArrayList<>();

				if (!Objects.isNull(classScheduleSearchDTO.getCountryId())) {
					Join<ClassSchedule, Country> countryJoin = root.join("country");
					final Predicate countryIdPredicate = criteriaBuilder.equal(countryJoin.get("id"),
							classScheduleSearchDTO.getCountryId());
					predicates.add(countryIdPredicate);
				}

				if (!StringUtils.isEmpty(classScheduleSearchDTO.getName())) {
					final Predicate namePredicate = criteriaBuilder.like(root.get("name"),
							"%" + classScheduleSearchDTO.getName() + "%");
					predicates.add(namePredicate);
				}

				final Predicate availableDatePredicate = criteriaBuilder
						.or(criteriaBuilder.greaterThan(root.get("startTime"), LocalDateTime.now()));
				predicates.add(availableDatePredicate);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}

		};
	}
}

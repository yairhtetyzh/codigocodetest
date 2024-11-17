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

import org.springframework.data.jpa.domain.Specification;

import com.demo.codetest.dto.PackageSearchDTO;
import com.demo.codetest.entity.Country;
import com.demo.codetest.entity.PackagePlan;

public class PackageSpecification {

	public static Specification<PackagePlan> getAvailablePackages(PackageSearchDTO packageSearchDTO) {

		return new Specification<PackagePlan>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<PackagePlan> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				final Collection<Predicate> predicates = new ArrayList<>();

				if (!Objects.isNull(packageSearchDTO.getCountryId())) {
					Join<PackagePlan, Country> countryJoin = root.join("country");
					final Predicate countryIdPredicate = criteriaBuilder.equal(countryJoin.get("id"),
							packageSearchDTO.getCountryId());
					predicates.add(countryIdPredicate);
				}

				final Predicate expirationPredicate = criteriaBuilder.or(
                        criteriaBuilder.isNull(root.get("expirationDate")),
                        criteriaBuilder.greaterThan(root.get("expirationDate"), LocalDateTime.now())
                );
				predicates.add(expirationPredicate);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}

		};
	}
}

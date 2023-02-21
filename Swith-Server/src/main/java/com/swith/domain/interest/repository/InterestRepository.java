package com.swith.domain.interest.repository;

import com.swith.domain.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
}

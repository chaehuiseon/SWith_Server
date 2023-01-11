package com.swith.domain.rating.repository;

import com.swith.domain.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {

    //Query("select ")







}

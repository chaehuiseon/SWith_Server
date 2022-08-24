package com.example.demo.src.repository;

import com.example.demo.src.entity.Application;
import com.example.demo.src.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {

    //Query("select ")







}

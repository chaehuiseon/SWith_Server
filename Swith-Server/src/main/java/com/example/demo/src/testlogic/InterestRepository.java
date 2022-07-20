package com.example.demo.src.testlogic;

import com.example.demo.src.entity.Interest;
import com.example.demo.src.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest,Integer> {
}

package com.example.demo.src.repository;

import com.example.demo.src.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("select u from User u " +
            "join fetch u.interest1 " +
            "join fetch u.interest2 " +
            "where u.email = ?1")
    // youngmin -> change
    User findByEmail(String email);
//    Optional<User> findByEmail(String email);

//    @Query("select u " +
//            "from User u " +
//            "join fetch u.registerList " +
//            "where u.userIdx = :userIdx ")
//    Optional<User> findByIdWithRegister(Long userIdx);

}

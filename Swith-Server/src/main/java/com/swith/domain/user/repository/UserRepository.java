package com.swith.domain.user.repository;

import com.swith.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByUserIdx(Long id);



    @Query(value = "update USER u " +
            "set u.averageStar = :averageStar " +
            "where u.userIdx = :userIdx",nativeQuery = true)
    Double updateAvgStar(@Param("averageStar") Double averageStar, @Param("userIdx") Long userIdx);

    @Query("select u.averageStar " +
            "from User u " +
            "where u.userIdx = :userIdx ")
    Double findUserStar(@Param("userIdx") Long userIdx);


}
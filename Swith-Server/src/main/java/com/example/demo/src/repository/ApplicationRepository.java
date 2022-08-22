package com.example.demo.src.repository;

import com.example.demo.src.entity.Application;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {


    @Query("select count(a) from Application a "+
            "where a.groupInfo.groupIdx = :groupIdx "+
            "and a.status = 1")
    Long findNumOfApplicants(@Param("groupIdx")Long groupIdx);


    @Query("select a "+
            "from Application a "+
            "join fetch a.user u "+
            "where a.groupInfo.groupIdx = :groupIdx and a.status = :status")
    List<Application> getApplicationListBy(@Param("groupIdx")Long groupIdx, @Param("status")Integer status);



    @Modifying(clearAutomatically = true)
    @Query(value = "update Application a " +
            "set a.status = :status " +
            "where a.applicationIdx = :applicationIdx and a.groupIdx = :groupIdx and a.status = 0 ",nativeQuery = true)
    Integer updateStatusOfApplication(@Param("status")Integer status, @Param("applicationIdx")Long applicationIdx, @Param("groupIdx")Long groupIdx);








}

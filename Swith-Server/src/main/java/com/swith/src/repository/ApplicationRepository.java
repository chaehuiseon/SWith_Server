package com.swith.src.repository;

import com.swith.domain.application.entity.Application;
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
            "where a.groupInfo.groupIdx = :groupIdx and a.status = :status ")
    List<Application> getApplicationListBy(@Param("groupIdx")Long groupIdx, @Param("status")Integer status);



    //nativequery는 진짜 SQL 문법기준으로 작성해야됨..
    @Modifying(clearAutomatically = true)
    @Query(value = "update APPLICATION a " +
            "set a.status = :reqstatus " +
            "where a.applicationIdx = :applicationIdx and a.groupIdx = :groupIdx and a.status = :status ",nativeQuery = true)
    Integer updateStatusOfApplication(@Param("reqstatus")Integer reqstatus, @Param("applicationIdx")Long applicationIdx,
                                      @Param("groupIdx")Long groupIdx, @Param("status") Integer status);

    @Query("select a " +
            "from Application a " +
            "join fetch a.groupInfo " +
            "where a.user.userIdx = :userIdx")
    List<Application> findByUserWithGroup(Long userIdx);
}

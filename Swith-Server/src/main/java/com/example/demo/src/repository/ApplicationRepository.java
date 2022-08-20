package com.example.demo.src.repository;

import com.example.demo.src.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {


    @Query("select count(a) from Application a "+
            "where a.groupInfo.groupIdx = :groupIdx "+
            "and a.status = 1")
    Long findNumOfApplicants(@Param("groupIdx")Long groupIdx);



}

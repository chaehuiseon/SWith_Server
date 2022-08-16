package com.example.demo.src.repository;

import com.example.demo.src.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {

    @Query("select a from Announcement a " +
            "where a.groupInfo.groupIdx in (:groupIdx) " +
            "and a.status = 0 " +
            "order by a.modifiedAt DESC")
    List<Announcement> findByGroupInfo_GroupIdxOrderByModifiedAtDesc(Long groupIdx);

    @Query("select a from Announcement a where a.groupInfo.groupIdx = :groupIdx and a.status = 0")
    List<Announcement> findByGroupIdx(Long groupIdx);

    @Modifying
    @Query("update Announcement a " +
            "set a.announcementContent = :announcementContent, " +
            "a.modifiedAt = :now " +
            "where a.announcementIdx = :announcementIdx")
    Integer updateById(Long announcementIdx, String announcementContent, LocalDateTime now);


    //상태만 비활성화로 바꾸는 로직,
    @Modifying
    @Query("update Announcement a set a.status = 1 where a.announcementIdx = :announcementIdx")
    Integer deleteByIdx(Long announcementIdx);
}

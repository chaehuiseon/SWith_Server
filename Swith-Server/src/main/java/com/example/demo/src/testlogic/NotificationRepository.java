package com.example.demo.src.testlogic;

import com.example.demo.src.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {

    @Query("select n from Notification n where n.user.userIdx = ?1")
    List<Notification> findAllByUser(Long userIdx);

    @Query("select n.notificationContent from Notification n where n.user.userIdx = ?1")
    List<String> findAllByUser2(Long userIdx);

//    @Query(value = "select notificationContent from Notification where ?1", nativeQuery = true)
//    List<String> findByUserId(int userIdx);



}

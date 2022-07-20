package com.example.demo.src.testlogic;

import com.example.demo.src.entity.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetNotiReq {


    private int userIdx; //알림 받는 사람 Idx
    private String notificationContent; //내용

}

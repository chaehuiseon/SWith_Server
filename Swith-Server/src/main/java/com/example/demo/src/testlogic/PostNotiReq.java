package com.example.demo.src.testlogic;


import com.example.demo.src.entity.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostNotiReq {

    private int userIdx;
    private String notificationContent;

}

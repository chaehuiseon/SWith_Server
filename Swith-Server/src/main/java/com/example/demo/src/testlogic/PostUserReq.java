package com.example.demo.src.testlogic;

import com.example.demo.src.entity.Interest;
import com.example.demo.src.entity.Register;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostUserReq {

    private String email;
    private String password;
    private int interest1; //관심 분류
    private int interest2; //관심 분류
    private String introduction; //소개글
    private String profileImgUrl; //프로필 사진의 Url , S3연결해야됨.
    private double averageStar; // 평점



}

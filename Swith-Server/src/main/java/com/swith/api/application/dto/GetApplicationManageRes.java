package com.swith.api.application.dto;


import com.swith.domain.application.entity.Application;
import lombok.*;

@Data
@Builder
public class GetApplicationManageRes {

    private Long userIdx;
    private String nickname;
    private String profileImgUrl;
    private Long applicationIdx;
    private String applicationContent;

//    public GetApplicationManageRes(Application application) {
//        this.userIdx = application.getUser().getUserIdx();
//        this.nickname = application.getUser().getNickname();
//        this.profileImgUrl = application.getUser().getProfileImgUrl();
//        this.applicationIdx = application.getApplicationIdx();
//        this.applicationContent = application.getApplicationContent();
//    }

    public static  GetApplicationManageRes from (Application application){
        return GetApplicationManageRes.builder()
                .userIdx(application.getUser().getUserIdx())
                .nickname(application.getUser().getNickname())
                .profileImgUrl(application.getUser().getProfileImgUrl())
                .applicationIdx(application.getApplicationIdx())
                .applicationContent(application.getApplicationContent())
                .build();

    }

}

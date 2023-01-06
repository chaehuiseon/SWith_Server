package com.swith.src.dto.response;


import com.swith.src.entity.Application;
import lombok.*;

@Data
public class GetApplicationManageRes {

    private Long userIdx;
    private String nickname;
    private String profileImgUrl;
    private Long applicationIdx;
    private String applicationContent;

    public GetApplicationManageRes(Application application) {
        this.userIdx = application.getUser().getUserIdx();
        this.nickname = application.getUser().getNickname();
        this.profileImgUrl = application.getUser().getProfileImgUrl();
        this.applicationIdx = application.getApplicationIdx();
        this.applicationContent = application.getApplicationContent();
    }
}

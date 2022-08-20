package com.example.demo.src.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetApplicationManageRes {

    private Long userIdx;
    private String nickname;
    private String profileImgUrl;
    private String applicationContent;

}

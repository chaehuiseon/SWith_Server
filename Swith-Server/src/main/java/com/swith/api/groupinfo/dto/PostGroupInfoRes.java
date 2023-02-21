package com.swith.api.groupinfo.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostGroupInfoRes {
    @ApiModelProperty(notes = "스터디 개설한 유저의 PK",example = "1")
    private Long groupIdx;

    public static PostGroupInfoRes from (Long groupIdx){
        return PostGroupInfoRes.builder()
                .groupIdx(groupIdx)
                .build();

    }
}

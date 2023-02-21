package com.swith.api.groupinfo.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class changeEndStatus {

    private Long groupIdx;
    private Integer status;
    private String title;


    public static changeEndStatus from (Long groupIdx, Integer status, String title){
        return changeEndStatus.builder()
                .groupIdx(groupIdx)
                .status(status)
                .title(title)
                .build();

    }

}

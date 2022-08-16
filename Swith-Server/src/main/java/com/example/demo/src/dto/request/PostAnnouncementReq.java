package com.example.demo.src.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostAnnouncementReq {

    @ApiModelProperty(notes = "스터디 그룹의 인덱스", example = "2")
    private Long groupIdx;

    @ApiModelProperty(notes = "공지사항 내용", example = "이번 달도 열심히 해봅시다!")
    private String announcementContent;
}

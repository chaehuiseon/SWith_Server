package com.swith.src.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatchAnnouncementReq {

    @ApiModelProperty(notes = "공지사항의 인덱스", example = "9")
    private Long announcementIdx;

    @ApiModelProperty(notes = "수정하고 싶은 공지사항 내용", example = "이번 달도 열심히 해봅시다!")
    private String announcementContent;
}

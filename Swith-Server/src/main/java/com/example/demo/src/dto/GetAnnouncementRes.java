package com.example.demo.src.dto;

import com.example.demo.src.entity.GroupInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetAnnouncementRes {
    @ApiModelProperty(notes = "공지사항의 인덱스", example = "3")
    private Long announcementIdx;

    @ApiModelProperty(notes = "공지사항 내용", example = "앞으로는 비대면으로 진행할 예정입니다.")
    private String announcementContent;

    @ApiModelProperty(notes = "공지사항 생성일", example = "[2022,08,01,17,30,(00)]")
    private LocalDateTime createdAt;
}

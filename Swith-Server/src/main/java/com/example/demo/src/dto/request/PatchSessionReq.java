package com.example.demo.src.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatchSessionReq {

    @ApiModelProperty(notes = "세션의 Idx", example = "21")
    private Long sessionIdx;

    @ApiModelProperty(notes = "변경할 회차 진행 시작 시각", example = "\"1997-09-12T12:30:30\"")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sessionStart;

    @ApiModelProperty(notes = "변경할 회차 진행 종료 시각", example = "\"1997-09-12T12:30:30\"")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sessionEnd;

    @ApiModelProperty(notes = "온라인 여부 ,0:온라인 1:오프라인", example = "1")
    private Integer online;

    @ApiModelProperty(notes = "스터디 진행 장소", example = "상도역 스타벅스")
    private String place;

    @ApiModelProperty(notes = "해당 회차의 변경할 학습 상세 내용", example = "안녕하세요")
    private String sessionContent;
}

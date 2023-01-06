package com.swith.src.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetGroupInfoSearchReq implements Serializable {

    private String title; //스터디 그룹의 제목

    private String regionIdx; //이거 다중선택하면 list로 보내나?

    private Integer interest1;

    private Integer interest2;
    private Long groupIdx;

    private Integer sortCond; //마감일이 최초값..

    //@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ClientTime;



}

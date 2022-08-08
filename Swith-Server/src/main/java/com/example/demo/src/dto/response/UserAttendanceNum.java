package com.example.demo.src.dto.response;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

public interface UserAttendanceNum {
    Long getUserIdx();
    String getNickname();
    Integer getStatus();
    Integer getAttendanceNum();
}

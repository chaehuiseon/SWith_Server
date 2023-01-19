package com.swith.api.common.dto;

import com.swith.global.error.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result", "pageInfo"})
public class BaseResponsePaging<T> {
    @JsonProperty("isSuccess")
    private HttpStatus isSuccess;
    private String message;
    private int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;
    private PagingRes pageInfo;

    // 페이징이 필요한 요청에 성공한 경우
    public BaseResponsePaging(T result, PagingRes pageInfo) {
        this.isSuccess = BaseResponseStatus.SUCCESS.getHttpStatus();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.result = result;
        this.pageInfo = pageInfo;
    }

    // 요청에 실패한 경우
    public BaseResponsePaging(BaseResponseStatus status) {
        this.isSuccess = status.getHttpStatus();
        this.message = status.getMessage();
        this.code = status.getCode();
    }

}
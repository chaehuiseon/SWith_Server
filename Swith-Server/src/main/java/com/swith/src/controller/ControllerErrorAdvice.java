//package com.example.demo.src.controller;
//
//import com.example.demo.src.dto.ErrorResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import java.io.IOException;
//
///**
// *  에러와 관련된 HTTP 응답을 관리하는 클래스입니다.
// */
//@ResponseBody
//@ControllerAdvice
//public class ControllerErrorAdvice {
//
//    /**
//     * InvalidTypeException이 발생했을 때 client에 에러코드 400을 반환하고 에러 메시지를 반환합니다.
//     * @return ErrorReponse 객체
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IOException.class)
//    public ErrorResponse handleInvalidTypeException() {
//        return new ErrorResponse("Invalid Type");
//    }
//}

package com.example.demo.config;

import lombok.Getter;


/*
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /*
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /*
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false, 2003, "권한이 없는 유저의 접근입니다."),
    NOT_EXIST_USER(false, 2004,"존재하지 않는 유저 또는 아이디 비번 확인"),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_CERTIFY_IMG(false,2014,"소속 인증을 위한 이미지를 등록해주세요."),
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false, 2017, "중복된 이메일입니다."),

    POST_USERS_EVENT_CREATION_FAIL(false,2018,"event생성 실패"),



    POST_USERS_EMPTY_PASSWORD(false, 2030, "비밀번호를 입력해주세요. "),
    POST_USERS_INVALID_PASSWORD(false, 2031, "비밀번호가 일치하지 않습니다."),

    //session
    POST_SESSION_NOT_ADMIN(false, 2040, "해당 유저가 그룹의 관리자가 아닙니다."),

    //event

    EVENT_EXISTS(false,2050,"evnet가 존재하지 않습니다."),
    PHOTO_EXISTS(false,2051,"evnet가 존재하지 않습니다."),
    PATCH_EVENT_CONTENTS_FAIL(false,2052,"event 내용 update 실패"),


    //auth

    PATCH_USER_CERTIFIED_FAIL(false,2080,"certify실패"),
    CHECK_USER_CERTIFIED_FAIL(false,2081,"certified 값 불러오기 실패 또는 유효하지 않은 값"),
    GET_UNCERTIFICATION_USER_FAIL(false,2082,"미인증 유저 리스트 불러오기 실패"),
    ERROR_FIND_EMAIL(false,2083,"가입하지 않은 이메일 입니다."),
    ERROR_FIND_USERIDX(false,2084,"email로 가입된 userIdx가 존재하지 않음."),
    PATCH_USER_PASSWORD(false,2085,"password patch실패"),


    /*
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false, 3014, "없는 아이디거나 비밀번호가 틀렸습니다."),


    /*
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false, 4014, "유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),


    // 5000 : 회차(SESSION)
    TEMP1(false, 9000, "conflict 방지용 1"),
    START_TIME_ERROR(false, 5000, "회차 시작 시간이 현재 시각보다 이전입니다."),
    NO_SESSION_INFO(false, 5001, "해당 그룹의 세션 정보가 없거나 유효하지 않은 groupIdx 입니다."),
    INAPPROPRIATE_START_TIME(false, 5002, "수정 요청된 시작시간이 현재 보다 이전입니다."),
    TIME_OVERLAPPED(false, 5003, "다른 회차의 시간대와 겹칩니다."),

    // 6000 : INVALID 오류
    TEMP2(false, 9000, "conflict 방지용 2"),
    INVALID_USER(false, 6000, "존재하지 않는 회원입니다."),
    INVALID_NICKNAME(false, 6001, "존재하지 않는 회원 닉네임입니다."),
    INVALID_GROUP(false,6002,"존재하지 않는 그룹입니다." ),
    INVALID_ANNOUNCEMENT(false,6003,"존재하지 않는 공지사항입니다."),
    INVALID_SESSION(false, 6004, "세션 정보가 없습니다."),
    INVALID_ATTENDANCE(false, 6005, "출석 정보가 없습니다."),
    NO_REGISTRATION_INFO(false, 6006, "유저의 스터디 그룹 가입 정보가 없습니다."),
    NO_GROUP_ATTENDANCE(false, 6007, "그룹의 출석 정보가 없습니다."),




    // 7000 : 공지사항
    MODIFY_FAIL_ANNOUNCEMENT(false, 7000, "수정에 실패하였습니다."),
    DELETE_FAIL_ANNOUNCEMENT(false, 7002, "삭제에 실패하였습니다."),
    ALREADY_DELETED_ANNOUNCEMENT(false, 7003, "해당 공지사항은 이미 삭제된 상태입니다."),
    TEMP3(false, 9000, "conflict 방지용 3"),


    MODIFY_FAIL_BUY_PLANT(false, 7011, "화분 선택에 실패하였습니다."),
    MODIFY_FAIL_SCORE(false, 7012, "화분 점수 변경에 실패하였습니다."),
    MODIFY_FAIL_LEVEL(false, 7013, "화분 단계 변경에 실패하였습니다."),


    INVALID_SCORE_PLANT(false, 7014, "선택한 화분의 점수가 0점입니다. 점수를 감소시킬 수 없습니다."),
    INVALID_LEVEL_PLANT(false, 7015, "동작을 수행할 화분의 단계가 0단계이므로 더 이상 단계와 점수를 감소시킬 수 없습니다."),

    INVALID_IDX_PLANT(false, 7016, "이미 선택된 화분입니다."),
    DUPLICATE_IDX_PLANT(false, 7017, "이미 보유한 화분입니다."),

    MODIFY_FAIL_PREMIUM(false, 7020, "프리미엄 계정 변경에 실패하였습니다."),
    MODIFY_FAIL_WITHDRAW(false, 7021, "청약철회에 실패하였습니다."),

    PREMIUM_USER(false, 7030, "프리미엄 계정 회원은 화분 성장치가 감소하지 않습니다."),
    MAXLEVEL_PLANT(false, 7031, "성장치가 MAX 단계에 도달한 화분은 성장치가 감소하지 않습니다."),
    SAD_STATUS_PLANT(false, 7032, "화분이 시무룩 상태입니다. 화분의 성장치를 증가시킬 수 없습니다."),


    // 8000 : 출석
    ALREADY_ATTENDED(false, 8000, "출석 상태가 결정되어 있습니다. 관리자만 변경 가능합니다."),
    FAIL_ATTEND(false, 8001, "출석 가능한 시간이 아닙니다."),
    EXIST_ATTEND_INFO(false, 8002, "이미 출석 데이터가 있습니다."),

    TEMP4(false, 9000, "conflict 방지용 4");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { // BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}

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
    NOT_EXIST_USER(false, 2004,"사용자를 찾을 수 없습니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_CERTIFY_IMG(false,2014,"소속 인증을 위한 이미지를 등록해주세요."),
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false, 2017, "중복된 이메일입니다."),




    POST_USERS_EMPTY_PASSWORD(false, 2030, "비밀번호를 입력해주세요. "),
    POST_USERS_INVALID_PASSWORD(false, 2031, "비밀번호가 일치하지 않습니다."),

    //session
    POST_SESSION_NOT_ADMIN(false, 2040, "해당 유저가 그룹의 관리자가 아닙니다."),

    //auth

    PATCH_USER_CERTIFIED_FAIL(false,2080,"certify실패"),
    CHECK_USER_CERTIFIED_FAIL(false,2081,"certified 값 불러오기 실패 또는 유효하지 않은 값"),
    GET_UNCERTIFICATION_USER_FAIL(false,2082,"미인증 유저 리스트 불러오기 실패"),
    ERROR_FIND_EMAIL(false,2083,"가입하지 않은 이메일 입니다."),
    ERROR_FIND_USERIDX(false,2084,"email로 가입된 userIdx가 존재하지 않음."),
    PATCH_USER_PASSWORD(false,2085,"password patch실패"),
    NOT_LOGIN(false,2086,"로그인 해주세요."),
    REFRESH_LOGOUT(false,2087,"리프레시 토큰이 아닌 엑세스 토큰을 사용해주세요."),
    ALREADY_LOGOUT(false,2088,"이미 로그아웃한 상태입니다."),


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
    ALREADY_DELETED_SESSION(false, 5004, "해당 세션은 이미 삭제된 상태입니다."),
    DELETE_FAIL_SESSION(false, 5005, "해당 세션은 삭제가 불가능합니다."),


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
    NO_GROUP_LEADER(false,6008,"그룹 리더가 아니기 때문에, 권한이 없습니다."),
    NO_APPLICATION_INFO(false, 6009, "유저의 지원서 정보가 없습니다."),
    INVALID_MEMO(false, 6010, "메모 정보가 없습니다."),




    // 7000 : 공지사항
    MODIFY_FAIL_ANNOUNCEMENT(false, 7000, "수정에 실패하였습니다."),
    DELETE_FAIL_ANNOUNCEMENT(false, 7002, "삭제에 실패하였습니다."),
    ALREADY_DELETED_ANNOUNCEMENT(false, 7003, "해당 공지사항은 이미 삭제된 상태입니다."),
    TEMP3(false, 9000, "conflict 방지용 3"),


    // 8000 : 출석, 메모
    ALREADY_ATTENDED(false, 8000, "출석 상태가 결정되어 있습니다. 관리자만 변경 가능합니다."),
    FAIL_ATTEND(false, 8001, "출석 가능한 시간이 아닙니다."),
    EXIST_ATTEND_INFO(false, 8002, "이미 출석 데이터가 있습니다."),
    ALREADY_EXIST(false, 8003, "메모가 이미 존재하여 생성할 수 없습니다."),

    TEMP4(false, 9000, "conflict 방지용 4"),


    //9000 : 신청

    FULL_NUM_OF_Applicants(false,9001,"신청 인원이 전부 다 찼습니다."),
    FAIL_SAVED_APPLICATION(false,9002,"서버 error. 가입 신청 실패"),
    FAIL_CHANGED_STATUS(false,9003,"상태 변경 실패"),
    DO_NOT_EXECUTE_CHANGE(false,9004,"변경 실행안함 : 이미 변경되었거나 잘못된 조건"),
    INVALID_STATUS(false,9005,"상태 변경 실패 : 유효하지 않은 값"),
    FAIL_REGISER(false, 9006, "가입승인 -> Regiest 등록 실패"),

    //9050 : 그룹
    FAIL_LOAD_GROUPINFO(false,9003,"스터디 그룹이 존재하지 않습니다."),
    FAIL_CLOSED_GROUPINFO(false,9051,"종료된 스터디 그룹입니다.");



    private final boolean isSuccess;
    private final int code;
    private final String message;



    private BaseResponseStatus(boolean isSuccess, int code, String message) { // BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}

package com.swith.global.error.exception;

import com.swith.global.error.BaseResponseStatus;

public class EntityNotFoundException extends BaseException{

    public EntityNotFoundException(BaseResponseStatus status) {
        super(status);
    }
}

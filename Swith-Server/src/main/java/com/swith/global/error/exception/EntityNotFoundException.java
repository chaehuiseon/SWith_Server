package com.swith.global.error.exception;

import com.swith.global.error.ErrorCode;

public class EntityNotFoundException extends BaseException{

    public EntityNotFoundException(ErrorCode status) {
        super(status);
    }
}

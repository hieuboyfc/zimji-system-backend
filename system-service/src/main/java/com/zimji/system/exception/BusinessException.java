package com.zimji.system.exception;

import com.zimji.system.utils.enums.ResponseCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessException extends RuntimeException {

    String code;
    String message;
    String title;
    Object[] args;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResponseCode.BUSINESS_EXCEPTION.getCode();
        this.title = ResponseCode.BUSINESS_EXCEPTION.getTitle();
        this.message = message;
    }

    public BusinessException(String code, String message, Object... args) {
        super(message);
        this.code = code;
        this.title = ResponseCode.BUSINESS_EXCEPTION.getTitle();
        this.message = message;
        this.args = args;
    }

    public BusinessException(String code, String title, String message, Object... args) {
        super(message);
        this.code = code;
        this.title = title;
        this.message = message;
        this.args = args;
    }

    public BusinessException(Integer code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code.toString();
        this.title = ResponseCode.BUSINESS_EXCEPTION.getTitle();
        this.message = message;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.title = ResponseCode.BUSINESS_EXCEPTION.getTitle();
        this.message = message;
    }

    public BusinessException(String code, String message, Throwable throwable) {
        super(throwable);
        this.code = code;
        this.title = ResponseCode.UN_CAPTURE_EXCEPTION.getTitle();
        this.message = message;
    }

    public BusinessException(Integer code, Throwable throwable) {
        super(throwable);
        this.code = code.toString();
        this.title = ResponseCode.UN_CAPTURE_EXCEPTION.getTitle();
        this.message = throwable.getLocalizedMessage();
    }

    public BusinessException(String code, Throwable throwable) {
        super(throwable);
        this.code = code;
        this.title = ResponseCode.UN_CAPTURE_EXCEPTION.getTitle();
        this.message = throwable.getLocalizedMessage();
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
        this.code = ResponseCode.UN_CAPTURE_EXCEPTION.getCode();
        this.title = ResponseCode.UN_CAPTURE_EXCEPTION.getTitle();
        this.message = throwable.getLocalizedMessage();
    }

}
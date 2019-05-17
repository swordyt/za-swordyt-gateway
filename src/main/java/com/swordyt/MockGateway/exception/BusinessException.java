package com.swordyt.MockGateway.exception;


import com.swordyt.MockGateway.dto.response.ResultModel;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException {

    private String message;

    private String errCode;

    public BusinessException(String message, String... paras) {
        this.message = String.format(message, (Object[]) paras);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BusinessException(Throwable cause) {
        this(cause.getMessage(), cause);
        this.message = cause.getMessage();
    }

    public BusinessException(String message) {
        this.message = message;
    }

    public BusinessException(String errCode,String message) {
        this.message = message;
        this.errCode = errCode;
    }

    public BusinessException(ResultModel.ERR_CODE err_code, String message) {
        this.message = message;
        this.errCode = err_code.getCode();
    }
}

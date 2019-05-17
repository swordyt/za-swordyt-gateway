package com.swordyt.MockGateway.dto.response;


import com.google.common.collect.Maps;
import lombok.Data;


@Data
public final class ResultModel<T> {

    private String returnCode;
    private String returnMsg;
    private T data;

    private ResultModel(String result, String message, T data) {
        this.returnCode = result;
        this.returnMsg = message;
        this.data = data;
    }

    public static<T> ResultModel<T> success() {
        return new ResultModel(ERR_CODE.SUCCESS.code, "SUCCESS", null);
    }

    public static<T> ResultModel<T> success(T data) {
        return new ResultModel<>(ERR_CODE.SUCCESS.code, "SUCCESS", data);
    }


    public static<T> ResultModel<T> success(String message, T data) {
        return new ResultModel<>(ERR_CODE.SUCCESS.code, message, data);
    }


    public static ResultModel failure(String returnCode, String message){
        return new ResultModel<>(returnCode, message, null);
    }

    public static ResultModel failure(ERR_CODE returnCode, String message){
        return new ResultModel<>(returnCode.code, message,null);
    }

        public static ResultModel failure() {
        return new ResultModel<>(ERR_CODE.FAIL.code, "FAILURE", null);
    }


    public static ResultModel failure(String message) {
        return new ResultModel<>(ERR_CODE.FAIL.code, message, null);
    }


    public enum ERR_CODE {
        SUCCESS("000000","成功"),
        PARAERROR("900001","参数错误"),
        INVALIDCOOKIE("999999","无效cookie"),
        FAIL("900000","失败");

        private String code;
        private String msg;
        ERR_CODE(String code,String msg) {
            this.code = code;
            this.msg = msg;
        }


        public String getCode() {
            return this.code;
        }

        public String getMsg() {
            return this.msg;
        }
    }

}

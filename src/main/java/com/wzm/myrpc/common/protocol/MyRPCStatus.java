package com.wzm.myrpc.common.protocol;

/**
 * 响应状态码
 * @author 东方雨倾
 * @since 1.0.0
 */
public enum MyRPCStatus {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"ERROR"),
    NOT_FOUND(404,"NOT FOUND");

    private int code;

    private String message;

    MyRPCStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}

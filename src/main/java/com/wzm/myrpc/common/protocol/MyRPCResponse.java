package com.wzm.myrpc.common.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应消息封装类
 *
 * @author 东方雨倾
 * @since 1.0.0
 */
public class MyRPCResponse implements Serializable {
    private static final long serialVersionUID = -4317845782629589997L;

    private MyRPCStatus status;

    private Map<String,String> headers = new HashMap<>();

    private Object returnValue;

    private Exception exception;

    public MyRPCResponse(MyRPCStatus status) {
        this.status = status;
    }

    public MyRPCStatus getStatus() {
        return status;
    }

    public void setStatus(MyRPCStatus status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String name) {
        return this.headers == null ? null : this.headers.get(name);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setHeaders(String name, String value) {
        this.headers.put(name,value);
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}

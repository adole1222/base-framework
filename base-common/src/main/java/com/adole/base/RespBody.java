package com.adole.base;

import java.io.Serializable;

public class RespBody implements Serializable {

    private String code;

    private String msg;

    private Object body;

    public final static String OK_CODE = "0";
    public final static String ERROR_CODE = "500";

    public RespBody(String code, String msg, Object body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    public static RespBody ok() {
        return new RespBody(OK_CODE, "", null);
    }

    public static RespBody ok(Object body) {
        return new RespBody(OK_CODE, "", body);
    }

    public static RespBody error(Object body) {
        return new RespBody(ERROR_CODE, "", body);
    }

    public static RespBody error(String errorCode, Object body) {
        return new RespBody(errorCode, "", body);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}

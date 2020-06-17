package com.adole.base;

import java.util.Arrays;

/**
 * 业务异常
 */
public class BizApplicationException extends RuntimeException {

    private final static String ERROR = "999999";

    private String errorCode;

    private String errorMsg;

    private Object[] arguments = new Object[0];

    public BizApplicationException(String errorCode) {
        this.errorCode = errorCode;
    }

    public BizApplicationException(String errorCode, Object... arguments) {
        this.errorCode = errorCode;
        this.arguments = Arrays.asList(arguments).toArray();
    }
}

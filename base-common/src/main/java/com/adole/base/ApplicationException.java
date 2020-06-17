package com.adole.base;

import java.util.Arrays;

/**
 * 程序异常
 */
public class ApplicationException extends RuntimeException {

    private final static String ERROR = "999999";

    private String errorCode;

    private String errorMsg;

    private Object[] arguments = new Object[0];

    public ApplicationException(String errorCode) {
        this.errorCode = errorCode;
    }

    public ApplicationException(String errorCode, Object... arguments) {
        this.errorCode = errorCode;
        this.arguments = Arrays.asList(arguments).toArray();
    }
}

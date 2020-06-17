package com.adole.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

    private static String SPLICE = " | ";

    public static void info(Class clazz, String title, Object message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isInfoEnabled()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(title).append(SPLICE).append(message.toString());
            logger.info(stringBuffer.toString());
        }
    }

    public static void debug(Class clazz, String title, Object message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isDebugEnabled()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(title).append(SPLICE).append(message.toString());
            logger.debug(stringBuffer.toString());
        }
    }

    public static void info(Class clazz, String title, Object message, Object... objects) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isInfoEnabled()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(title).append(SPLICE).append(message.toString());
            logger.info(stringBuffer.toString(), objects);
        }
    }

    public static void debug(Class clazz, String title, Object message, Object... objects) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isDebugEnabled()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(title).append(SPLICE).append(message.toString());
            logger.debug(stringBuffer.toString(), objects);
        }
    }

    public static void error(Class clazz, String title, Object message, Throwable e) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isErrorEnabled()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(title).append(SPLICE).append(message.toString());
            logger.error(stringBuffer.toString(), e);
        }
    }
}

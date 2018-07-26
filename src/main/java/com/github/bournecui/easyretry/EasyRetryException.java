package com.github.bournecui.easyretry;

/**
 * Created by cuilei05 on 2018/7/25.
 */
public class EasyRetryException extends RuntimeException {
    public EasyRetryException() {
        super();
    }

    public EasyRetryException(String message) {
        super(message);
    }

    public EasyRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EasyRetryException(Throwable cause) {
        super(cause);
    }

    protected EasyRetryException(String message, Throwable cause,
                                 boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

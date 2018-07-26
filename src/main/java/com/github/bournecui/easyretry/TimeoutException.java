package com.github.bournecui.easyretry;

public class TimeoutException extends EasyRetryException {
    public TimeoutException() {
    }

    public TimeoutException(String message) {
        super(message);
    }
}

package com.changhong.adsystem.http.image.loader.exception;

/**
 */
public class ThreadKillException extends RuntimeException {

    public ThreadKillException() {
    }

    public ThreadKillException(String detailMessage) {
        super(detailMessage);
    }

    public ThreadKillException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ThreadKillException(Throwable throwable) {
        super(throwable);
    }

}

package com.sthwin.webflux;

/**
 * Created by User
 * Date: 2020. 6. 19. 오전 8:39
 */
public class TestRuntimeException extends RuntimeException {
    public TestRuntimeException(Throwable e) {
        super(e);
    }

    public TestRuntimeException(String msg) {
        super(msg);
    }

    public TestRuntimeException(String msg, Throwable e) {
        super(msg, e);
    }
}

package ru.openblocks.ratelimiter.exception;

public class LimiterNotFoundException extends RuntimeException {

    public LimiterNotFoundException() {
        super();
    }

    public LimiterNotFoundException(String message) {
        super(message);
    }
}

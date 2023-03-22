package ru.openblocks.ratelimiter.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import ru.openblocks.ratelimiter.common.ErrorMessage;
import ru.openblocks.ratelimiter.exception.LimiterNotFoundException;

@RestControllerAdvice
public class ExceptionHandlers {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LimiterNotFoundException.class)
    public Mono<ErrorMessage> limiterNotFoundException(LimiterNotFoundException ex) {
        return Mono.just(
                ErrorMessage.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }
}

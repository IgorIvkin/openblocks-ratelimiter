package ru.openblocks.ratelimiter.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {

    /**
     * Сообщение об ошибке.
     */
    private String message;
}

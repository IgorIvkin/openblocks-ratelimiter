package ru.openblocks.ratelimiter.common;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
@Builder
public class RateLimiterSetup {

    /**
     * Число запросов.
     */
    private Integer limit;

    /**
     * Диапазон, в котором разрешено данное число запросов.
     * Возможные значения:
     *   HOURS - часы
     *   MINUTES - минуты
     *   SECONDS - секунды
     */
    private TimeUnit unit;
}

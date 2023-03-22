package ru.openblocks.ratelimiter.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RateLimiterSetup {

    /**
     * Число запросов в минуту.
     */
    private Integer limit;
}

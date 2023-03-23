package ru.openblocks.ratelimiter.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.openblocks.ratelimiter.common.RateLimiterSetup;
import ru.openblocks.ratelimiter.common.RateLimiterTimeUnit;
import ru.openblocks.ratelimiter.config.RateLimiterConfig;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Юнит-тесты на сервис обслуживания рейт-лимитеров")
public class RateLimiterServiceTests {

    private static final String BASIC_LIMITER_NAME = "basic";

    @MockBean
    private RateLimiterConfig rateLimiterConfig;

    @SpyBean
    private RateLimiterService rateLimiterService;

    @Test
    @DisplayName("Проверка доступности ресурса - основной сценарий")
    public void checkRateLimit_genericTest() {
        when(rateLimiterConfig.getLimiters()).thenReturn(getLimiters());

        rateLimiterService.initialize();
        rateLimiterService.updateRateLimits(RateLimiterTimeUnit.MINUTES);

        assertEquals(Boolean.TRUE, rateLimiterService.checkRateLimit(BASIC_LIMITER_NAME).block());
        assertEquals(Boolean.TRUE, rateLimiterService.checkRateLimit(BASIC_LIMITER_NAME).block());
        assertEquals(Boolean.TRUE, rateLimiterService.checkRateLimit(BASIC_LIMITER_NAME).block());
        assertEquals(Boolean.FALSE, rateLimiterService.checkRateLimit(BASIC_LIMITER_NAME).block());

        rateLimiterService.updateRateLimits(RateLimiterTimeUnit.MINUTES);
        assertEquals(Boolean.TRUE, rateLimiterService.checkRateLimit(BASIC_LIMITER_NAME).block());
    }

    private Map<String, RateLimiterSetup> getLimiters() {
        return Map.of(
                BASIC_LIMITER_NAME, RateLimiterSetup.builder()
                        .limit(3)
                        .unit(RateLimiterTimeUnit.MINUTES)
                        .build()
        );
    }
}

package ru.openblocks.ratelimiter.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Интерационные тесты на сервис обслуживания рейт-лимитеров")
public class RateLimiterBucketServiceIT {

    private static final String BASIC_LIMITER_NAME = "basic";

    @Autowired
    private RateLimiterService rateLimiterService;

    @Test
    @DisplayName("Проверка доступности ресурса - основной сценарий")
    public void checkRateLimits_genericTest() throws InterruptedException {
        assertEquals(Boolean.TRUE, rateLimiterService.checkRateLimit(BASIC_LIMITER_NAME).block());
        assertEquals(Boolean.TRUE, rateLimiterService.checkRateLimit(BASIC_LIMITER_NAME).block());
        assertEquals(Boolean.FALSE, rateLimiterService.checkRateLimit(BASIC_LIMITER_NAME).block());

        Thread.sleep(1000L);

        assertEquals(Boolean.TRUE, rateLimiterService.checkRateLimit(BASIC_LIMITER_NAME).block());
    }
}

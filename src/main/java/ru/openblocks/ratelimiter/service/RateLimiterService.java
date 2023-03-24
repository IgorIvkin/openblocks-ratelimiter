package ru.openblocks.ratelimiter.service;

import jakarta.annotation.PostConstruct;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.openblocks.ratelimiter.common.RateLimiterBucket;
import ru.openblocks.ratelimiter.common.RateLimiterSetup;
import ru.openblocks.ratelimiter.config.RateLimiterConfig;
import ru.openblocks.ratelimiter.exception.LimiterNotFoundException;

/**
 * Этот сервис предназначен для работы с лимитами, он инициализирует "бакеты" для токенов,
 * проверяет доступность токенов в "бакетах", а также предоставляет функциональность по
 * генерации новых токенов (эта функциональность вызывается по расписанию).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RateLimiterConfig rateLimiterConfig;

    private final Map<String, RateLimiterBucket> buckets = new HashMap<>();

    private final Clock clock = Clock.systemDefaultZone();

    @PostConstruct
    public void initialize() {
        initializeTokenBuckets();
    }

    /**
     * Возвращает статус доступности рейт-лимитера.
     * Если возвращается false, то это значит, что лимиты исчерпаны.
     *
     * @param limiterName название рейт-лимитера
     * @return статус, доступны ли еще запросы по этому лимитеру
     */
    public Mono<Boolean> checkRateLimit(String limiterName) {
        final RateLimiterBucket bucket = buckets.get(limiterName);
        if (bucket == null) {
            throw new LimiterNotFoundException("Cannot find rate limiter by name: " + limiterName);
        }
        return Mono.just(bucket.getToken() >= 0);
    }

    private void initializeTokenBuckets() {
        Map<String, RateLimiterSetup> limiters = rateLimiterConfig.getLimiters();
        limiters.forEach((limiterName, limiterSetup) -> {

            final Integer limit = limiterSetup.getLimit();
            final TimeUnit unit = limiterSetup.getUnit();
            if (limit == null) {
                throw new IllegalStateException("Cannot get limit for limiter: " + limiterName);
            }
            log.info("Initialize token bucket \"{}\" with limit {} requests / {}", limiterName, limit, unit);

            final RateLimiterBucket bucket =
                    RateLimiterBucket.builder()
                            .tokens(Long.valueOf(limit))
                            .clock(clock)
                            .lastCallTime(clock.millis())
                            .limit(limit)
                            .unit(unit)
                            .build();
            buckets.put(limiterName, bucket);
        });
    }
}

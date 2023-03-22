package ru.openblocks.ratelimiter.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.QueueUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
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

    /**
     * Значение токена по умолчанию.
     */
    private static final Long DEFAULT_TOKEN_VALUE = 1L;

    private final RateLimiterConfig rateLimiterConfig;

    private final Map<String, Queue<Long>> buckets = new HashMap<>();

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
        Queue<Long> bucket = buckets.get(limiterName);
        if (bucket == null) {
            throw new LimiterNotFoundException("Cannot find rate limiter by name: " + limiterName);
        }

        final Long token = bucket.poll();
        return Mono.just(token != null);
    }

    /**
     * Обновляет лимиты в существующих рейт-лимитерах, давая возможность продолжить
     * работу с использованием этих рейт-лимитеров. Этот метод вызывается по расписанию.
     */
    public void updateRateLimits() {
        Map<String, RateLimiterSetup> limiters = rateLimiterConfig.getLimiters();
        limiters.forEach((limiterName, limiter) -> {

            final Integer limit = limiter.getLimit();
            if (limit == null) {
                throw new IllegalStateException("Cannot update limit for " + limiterName + ", limit is null");
            }
            Queue<Long> bucket = buckets.get(limiterName);
            if (bucket == null) {
                throw new IllegalStateException("Cannot update limit for " + limiterName + ", no such bucket");
            }

            bucket.addAll(createNewTokens(limit));

        });
    }

    private void initializeTokenBuckets() {
        Map<String, RateLimiterSetup> limiters = rateLimiterConfig.getLimiters();
        limiters.forEach((limiterName, limiter) -> {

            final Integer limit = limiter.getLimit();
            if (limit == null) {
                throw new IllegalStateException("Cannot get limit for limiter: " + limiterName);
            }
            log.info("Initialize token bucket \"{}\" with limit {} requests/min", limiterName, limit);

            CircularFifoQueue<Long> bucket = new CircularFifoQueue<>(limit);
            Queue<Long> sychronizedQueue = QueueUtils.synchronizedQueue(bucket);
            buckets.put(limiterName, sychronizedQueue);
        });
    }

    private List<Long> createNewTokens(Integer size) {
        return new ArrayList<>(Collections.nCopies(size, DEFAULT_TOKEN_VALUE));
    }
}

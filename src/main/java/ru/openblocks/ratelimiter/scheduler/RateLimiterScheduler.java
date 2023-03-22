package ru.openblocks.ratelimiter.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.openblocks.ratelimiter.common.RateLimiterTimeUnit;
import ru.openblocks.ratelimiter.service.RateLimiterService;

/**
 * Этот компонент раз в минуту обновляет лимиты у существующих в сервисе рейт-лимитеров.
 */
@Component
@RequiredArgsConstructor
public class RateLimiterScheduler {

    private final RateLimiterService rateLimiterService;

    /**
     * Обновляет лимиты по каждому рейт-лимитеру раз в минуту.
     */
    @Scheduled(fixedRate = 60000L)
    public void updateTokensEveryMinute() {
        rateLimiterService.updateRateLimits(RateLimiterTimeUnit.MINUTES);
    }

    /**
     * Обновляет лимиты по каждому рейт-лимитеру раз в секунду.
     */
    @Scheduled(fixedRate = 1000L)
    public void updateTokensEverySecond() {
        rateLimiterService.updateRateLimits(RateLimiterTimeUnit.SECONDS);
    }

    /**
     * Обновляет лимиты по каждому рейт-лимитеру раз в час.
     */
    @Scheduled(fixedRate = 3600000L)
    public void updateTokensEveryHour() {
        rateLimiterService.updateRateLimits(RateLimiterTimeUnit.HOURS);
    }
}

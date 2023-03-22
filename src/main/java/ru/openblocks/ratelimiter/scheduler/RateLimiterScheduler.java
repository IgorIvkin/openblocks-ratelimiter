package ru.openblocks.ratelimiter.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
    public void updateTokens() {
        rateLimiterService.updateRateLimits();
    }
}

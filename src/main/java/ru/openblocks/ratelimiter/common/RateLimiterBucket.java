package ru.openblocks.ratelimiter.common;

import lombok.Builder;
import lombok.Data;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

@Data
@Builder
public class RateLimiterBucket {

    /**
     * Текущее количество токенов в бакете.
     */
    private long tokens;

    /**
     * Последний раз, когда вызывали обращение к этому рейт-лимитеру.
     */
    private long lastCallTime;

    /**
     * Число запросов.
     */
    private int limit;

    /**
     * Диапазон, в котором разрешено данное число запросов.
     */
    private TimeUnit unit;

    /**
     * Часы для расчета текущего момента времени.
     */
    private Clock clock;

    /**
     * Уменьшает число токенов на единицу и возвращает текущее значение.
     *
     * @return текущее количество токенов в бакете.
     */
    public synchronized long getToken() {
        updateTokens();
        return --tokens;
    }

    private void updateTokens() {
        final long currentTime = clock.millis();
        if (currentTime - lastCallTime > unit.toMillis(1)) {
            lastCallTime = currentTime;
            tokens = limit;
        }
    }
}

package ru.openblocks.ratelimiter.config;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.openblocks.ratelimiter.common.RateLimiterSetup;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimiterConfig {

    /**
     * Словарь отдельных рейт-лимитеров, которые определяются названиями - строковыми ключами.
     */
    private Map<String, RateLimiterSetup> limiters;

}

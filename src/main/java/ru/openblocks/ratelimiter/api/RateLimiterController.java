package ru.openblocks.ratelimiter.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.openblocks.ratelimiter.service.RateLimiterService;

@RestController
@RequestMapping("/api/v1/rate-limits")
@RequiredArgsConstructor
public class RateLimiterController {

    private final RateLimiterService rateLimiterService;

    @GetMapping("/{limiterName}")
    public Mono<Boolean> checkRateLimit(@PathVariable String limiterName) {
        return rateLimiterService.checkRateLimit(limiterName);
    }
}

package ru.openblocks.ratelimiter.common;

/**
 * Временной диапазон, в котором применяется ограничение доступа.
 */
public enum RateLimiterTimeUnit {

    SECONDS("seconds"),

    MINUTES("minutes"),

    HOURS("hours");

    private final String strValue;

    RateLimiterTimeUnit(String strValue) {
        this.strValue = strValue;
    }

    public String getStrValue() {
        return strValue;
    }
}

package com.dirijable.labs.lms.dto.cache;

import java.util.List;

public record GenericCacheKey(
        List<Object> params,
        Class<?> type
) implements CacheKey {
    public static GenericCacheKey of(List<Object> params, Class<?> type) {
        return new GenericCacheKey(List.copyOf(params), type);
    }
}
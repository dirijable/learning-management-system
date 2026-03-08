package com.dirijable.labs.lms.dto.cache;

import java.util.List;

public record GenericCacheKey(
        List<Object> params
) implements CacheKey {
    public static GenericCacheKey of(List<Object> params) {
        return new GenericCacheKey(List.copyOf(params));
    }
}
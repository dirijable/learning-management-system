package com.dirijable.labs.lms.cache;

import com.dirijable.labs.lms.dto.cache.CacheKey;

import java.util.Optional;

public interface Cache<V> {

    Optional<V> get(CacheKey key);

    void put(CacheKey key, V value);

    void invalidateCache();

}

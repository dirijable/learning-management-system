package com.dirijable.labs.lms.cache;

import com.dirijable.labs.lms.dto.cache.CacheKey;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class HashMapCache implements InMemoryCache {

    private final Map<CacheKey, Object> cache = new HashMap<>();

    @Override
    public <V> Optional<V> get(CacheKey key, Class<V> valueClass) {
        return Optional.ofNullable(cache.get(key))
                .filter(valueClass::isInstance)
                .map(valueClass::cast);
    }

    @Override
    public <V> void put(CacheKey key, V value) {
        cache.put(key, value);
    }

    @Override
    public void invalidateCache() {
        cache.clear();
    }
}

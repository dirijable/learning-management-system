package com.dirijable.labs.lms.cache;

import com.dirijable.labs.lms.dto.cache.CacheKey;
import com.dirijable.labs.lms.dto.page.PageResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Scope(value = "prototype")
public class PageCache<V> implements InMemoryCache<PageResponse<V>> {

    private final Map<CacheKey, PageResponse<V>> cache = new HashMap<>();

    @Override
    public Optional<PageResponse<V>> get(CacheKey key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public void put(CacheKey key, PageResponse<V> value) {
        cache.put(key, value);
    }

    @Override
    public void invalidateCache() {
        cache.clear();
    }
}

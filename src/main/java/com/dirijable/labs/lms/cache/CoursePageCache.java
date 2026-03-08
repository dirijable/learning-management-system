package com.dirijable.labs.lms.cache;

import com.dirijable.labs.lms.dto.cache.CacheKey;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.dto.page.PageResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CoursePageCache implements InMemoryCache<PageResponse<CourseResponseDto>> {

    private final Map<CacheKey, PageResponse<CourseResponseDto>> cache = new HashMap<>();

    @Override
    public Optional<PageResponse<CourseResponseDto>> get(CacheKey key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public void put(CacheKey key, PageResponse<CourseResponseDto> value) {
        cache.put(key, value);
    }

    @Override
    public void invalidateCache() {
        cache.clear();
    }
}

package com.dirijable.labs.lms.dto.page;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PageResponse<T> {
    private List<T> content;
    private Metadata metadata;

    public static <T> PageResponse<T> of(Page<T> page) {
        Metadata metadata = new Metadata(
                page.getNumber(),
                page.getTotalPages(),
                page.getSize(),
                page.getNumberOfElements()
        );
        return new PageResponse<>(page.getContent(), metadata);
    }

    public record Metadata(
            int page,
            int totalPages,
            int size,
            int totalElements
    ) {
    }
}

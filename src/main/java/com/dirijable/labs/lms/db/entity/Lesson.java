package com.dirijable.labs.lms.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    private Long id;

    private String title;

    private String content;

    private Integer durationMinutes;

    private Course course;
}
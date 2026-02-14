package com.dirijable.labs.lms.db.entity;

import lombok.*;

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
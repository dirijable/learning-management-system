package com.dirijable.labs.lms.db.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Long id;

    private String name;

    @Builder.Default
    private List<Course> courses = new ArrayList<>();
}
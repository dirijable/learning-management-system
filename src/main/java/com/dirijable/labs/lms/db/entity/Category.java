package com.dirijable.labs.lms.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
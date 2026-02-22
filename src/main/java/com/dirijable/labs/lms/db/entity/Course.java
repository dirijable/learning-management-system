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
public class Course {
    private Long id;

    private String name;

    private String description;

    private Instructor instructor;

    private Category category;

    @Builder.Default
    private List<Lesson> lessons = new ArrayList<>();

    @Builder.Default
    private List<Student> students = new ArrayList<>();

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setCourse(this);
    }


}
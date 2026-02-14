package com.dirijable.labs.lms.db.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Instructor extends BaseUser{

    private String description;

    private Specialization specialization;

    @Builder.Default
    private List<Course> courses = new ArrayList<>(); //потом ванТуМэни сделать
}


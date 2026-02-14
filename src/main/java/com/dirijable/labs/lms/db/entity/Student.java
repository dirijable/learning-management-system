package com.dirijable.labs.lms.db.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student extends BaseUser{

    private String email;

    @Builder.Default
    private List<Course> courses = new ArrayList<>(); //потом мб мэниТуМэни сделать
}
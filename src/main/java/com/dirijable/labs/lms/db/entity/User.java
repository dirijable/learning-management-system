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
public class User {
    private Long id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private UserRole role;

    private List<Course> courses = new ArrayList<>();
}
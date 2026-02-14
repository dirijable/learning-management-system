package com.dirijable.labs.lms.db.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseUser {
    private Long id;

    private String firstname;

    private String lastname;
}

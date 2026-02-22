package com.dirijable.labs.lms.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public abstract class BaseUser {
    private Long id;

    private String firstName;

    private String lastName;
}

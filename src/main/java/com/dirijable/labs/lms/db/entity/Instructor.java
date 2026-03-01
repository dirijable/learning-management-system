package com.dirijable.labs.lms.db.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Instructor{

    private Long id;

    private String firstName;

    private String lastName;

    private String description;

    private String specialization;
}


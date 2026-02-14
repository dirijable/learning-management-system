package com.dirijable.labs.lms.db.entity;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Specialization {
    private Long id;

    private String description;

    private String name;
}


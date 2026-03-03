package com.dirijable.labs.lms.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long expiration;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email",
            referencedColumnName = "email")
    private User user;
}

package com.dirijable.labs.lms.db.repository;

import com.dirijable.labs.lms.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}

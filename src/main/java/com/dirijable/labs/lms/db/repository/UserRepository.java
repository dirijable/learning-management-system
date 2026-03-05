package com.dirijable.labs.lms.db.repository;

import com.dirijable.labs.lms.db.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "courses")
    @Query("""
            SELECT u FROM User u
            WHERE u.id = :id
            """)
    Optional<User> findByIdOptimized(Long id);

    @EntityGraph(attributePaths = "courses")
    @Query("SELECT u FROM User u")
    List<User> findAllOptimized();
}

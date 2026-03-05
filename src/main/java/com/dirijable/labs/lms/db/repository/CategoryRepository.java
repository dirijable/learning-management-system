package com.dirijable.labs.lms.db.repository;

import com.dirijable.labs.lms.db.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @EntityGraph(attributePaths = "courses")
    @Query("""
                SELECT c FROM Category c
                WHERE c.id = :id AND c.isDeleted = false
            """)
    Optional<Category> findByIdOptimized(Long id);

    @EntityGraph(attributePaths = "courses")
    @Query("""
                SELECT c FROM Category c
                WHERE c.isDeleted = false
            """)
    List<Category> findAllOptimized();
}

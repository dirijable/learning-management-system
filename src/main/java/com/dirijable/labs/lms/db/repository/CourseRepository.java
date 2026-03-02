package com.dirijable.labs.lms.db.repository;

import com.dirijable.labs.lms.db.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);

    @Query("""
            SELECT co FROM Course co
            JOIN fetch co.category ca
            WHERE ca.name = :categoryName
            """)
    List<Course> findAllByCategoryName(String categoryName);
}

package com.dirijable.labs.lms.db.repository;

import com.dirijable.labs.lms.db.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByName(String name);

    Optional<Course> findById(Long id);

    @EntityGraph(attributePaths = {"instructor", "category", "lessons"})
    @Query("SELECT c FROM Course c WHERE c.id = :id")
    Optional<Course> findFullByIdOptimized(Long id);
}

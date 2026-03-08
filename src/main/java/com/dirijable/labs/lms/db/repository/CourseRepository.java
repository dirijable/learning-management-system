package com.dirijable.labs.lms.db.repository;

import com.dirijable.labs.lms.db.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
            SELECT c FROM Course c
            JOIN FETCH c.category cat
            WHERE cat.name = :categoryName
            """)
    Page<Course> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

    @Query(value = """
           SELECT c.* FROM courses c
           JOIN categories cat ON c.category_id = cat.id
           WHERE cat.name = :categoryName
           """,
            nativeQuery = true)
    Page<Course> findByCategoryNameNative(@Param("categoryName") String categoryName, Pageable pageable);

    Optional<Course> findByName(String name);

    Optional<Course> findById(Long id);

    @EntityGraph(attributePaths = {"instructor", "category", "lessons"})
    @Query("SELECT c FROM Course c WHERE c.id = :id")
    Optional<Course> findFullByIdOptimized(Long id);

    @EntityGraph(attributePaths = {"instructor", "category"})
    @Query("SELECT c FROM Course c")
    Page<Course> findAllOptimized(Pageable pageable);
}

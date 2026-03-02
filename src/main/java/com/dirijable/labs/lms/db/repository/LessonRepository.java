package com.dirijable.labs.lms.db.repository;

import com.dirijable.labs.lms.db.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("""
            SELECT l FROM Lesson l
            JOIN l.course c
            WHERE c.id = :courseId
            """)
    List<Lesson> findAllByCourseId(Long courseId);

    boolean existsByTitleAndCourseId(String title, Long courseId);
}

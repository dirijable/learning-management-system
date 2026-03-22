package com.dirijable.labs.lms.db.repository;

import com.dirijable.labs.lms.db.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Page<Lesson> findAllByCourseId(Long courseId, Pageable pageable);

    boolean existsByTitleAndCourseId(String title, Long courseId);
}

package com.dirijable.labs.lms.repository;

import com.dirijable.labs.lms.db.entity.Course;

import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long>{
    Optional<Course> findByName(String name);
}

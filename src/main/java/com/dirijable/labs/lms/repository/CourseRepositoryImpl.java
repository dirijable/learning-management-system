package com.dirijable.labs.lms.repository;

import com.dirijable.labs.lms.db.entity.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    private Long nextId = 1L;
    private List<Course> courses = new ArrayList<>();

    @Override
    public synchronized Course save(Course entity) {
        entity.setId(nextId);
        nextId = nextId + 1;
        courses.add(entity);
        return entity;
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courses.stream()
                .filter(course -> course.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Course> findByName(String name) {
        return courses.stream()
                .filter(course -> course.getName().equals(name))
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        courses.removeIf(specialization -> specialization.getId().equals(id));

    }

}

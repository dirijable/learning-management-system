package com.dirijable.labs.lms.db.repository;

import com.dirijable.labs.lms.db.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}

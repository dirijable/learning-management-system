package com.dirijable.labs.lms.service.instructor;

import com.dirijable.labs.lms.dto.instructor.InstructorCreateDto;
import com.dirijable.labs.lms.dto.instructor.InstructorResponseDto;
import com.dirijable.labs.lms.dto.instructor.InstructorUpdateDto;

import java.util.List;

public interface InstructorService {

    List<InstructorResponseDto> findAll();

    InstructorResponseDto findById(Long instructorId);

    InstructorResponseDto save(InstructorCreateDto createDto);

    InstructorResponseDto update(InstructorUpdateDto updateDto, Long instructorId);

    void delete(Long instructorId);
}

package com.dirijable.labs.lms.service.instructor;

import com.dirijable.labs.lms.db.entity.Instructor;
import com.dirijable.labs.lms.db.repository.InstructorRepository;
import com.dirijable.labs.lms.dto.instructor.InstructorCreateDto;
import com.dirijable.labs.lms.dto.instructor.InstructorResponseDto;
import com.dirijable.labs.lms.dto.instructor.InstructorUpdateDto;
import com.dirijable.labs.lms.exception.business.notfound.InstructorNotFoundException;
import com.dirijable.labs.lms.mapper.InstructorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;
    private static final String INSTRUCTOR_NOT_FOUND = "Instructor with id = '%d' not found";

    @Override
    public List<InstructorResponseDto> findAll() {
        return instructorRepository.findAll()
                .stream()
                .map(instructorMapper::toResponse)
                .toList();
    }

    @Override
    public InstructorResponseDto findById(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .map(instructorMapper::toResponse)
                .orElseThrow(() -> new InstructorNotFoundException(INSTRUCTOR_NOT_FOUND.formatted(instructorId)));
    }

    @Override
    @Transactional
    public InstructorResponseDto save(InstructorCreateDto createDto) {
        Instructor instructor = instructorMapper.toEntity(createDto);
        return instructorMapper.toResponse(instructorRepository.save(instructor));
    }

    @Override
    @Transactional
    public InstructorResponseDto update(InstructorUpdateDto updateDto, Long instructorId) {
        return instructorRepository.findById(instructorId)
                .map(instructor -> {
                    instructorMapper.updateEntity(updateDto, instructor);
                    return instructor;
                })
                .map(instructorMapper::toResponse)
                .orElseThrow(() -> new InstructorNotFoundException(INSTRUCTOR_NOT_FOUND.formatted(instructorId)));
    }

    @Override
    @Transactional
    public void delete(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new InstructorNotFoundException(INSTRUCTOR_NOT_FOUND.formatted(instructorId)));
        instructorRepository.delete(instructor);
    }
}

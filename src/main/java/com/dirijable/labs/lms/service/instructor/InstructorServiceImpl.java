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
@Transactional(readOnly = true)
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;

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
                .orElseThrow(() -> new InstructorNotFoundException("Instructor not found with id: " + instructorId));
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
                .orElseThrow(() -> new InstructorNotFoundException("Instructor not found with id: " + instructorId));
    }

    @Override
    @Transactional
    public void delete(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor not found with id: " + instructorId));
        instructorRepository.delete(instructor);
    }
}

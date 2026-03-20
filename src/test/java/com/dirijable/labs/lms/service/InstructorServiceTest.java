package com.dirijable.labs.lms.service;

import com.dirijable.labs.lms.db.entity.Instructor;
import com.dirijable.labs.lms.db.repository.InstructorRepository;
import com.dirijable.labs.lms.dto.instructor.InstructorCreateDto;
import com.dirijable.labs.lms.dto.instructor.InstructorResponseDto;
import com.dirijable.labs.lms.dto.instructor.InstructorUpdateDto;
import com.dirijable.labs.lms.exception.business.notfound.InstructorNotFoundException;
import com.dirijable.labs.lms.mapper.InstructorMapper;
import com.dirijable.labs.lms.service.instructor.InstructorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private InstructorMapper instructorMapper;

    @InjectMocks
    private InstructorServiceImpl instructorService;

    private static final Long ID = 1L;
    private Instructor instructor;
    private InstructorResponseDto responseDto;

    @BeforeEach
    void setUp() {
        instructor = Instructor.builder()
                .id(ID)
                .firstName("John")
                .lastName("Doe")
                .build();

        responseDto = new InstructorResponseDto(ID, "John", "Doe", "Desc", "Spec");
    }

    @Test
    void findAll_ReturnsList() {
        when(instructorRepository.findAll()).thenReturn(List.of(instructor));
        when(instructorMapper.toResponse(instructor)).thenReturn(responseDto);

        List<InstructorResponseDto> result = instructorService.findAll();

        assertThat(result).hasSize(1);
        verify(instructorRepository).findAll();
    }

    @Test
    void findById_Success() {
        when(instructorRepository.findById(ID)).thenReturn(Optional.of(instructor));
        when(instructorMapper.toResponse(instructor)).thenReturn(responseDto);

        InstructorResponseDto result = instructorService.findById(ID);

        assertNotNull(result);
        assertEquals(ID, result.id());
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(instructorRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(InstructorNotFoundException.class, () -> instructorService.findById(ID));
    }

    @Test
    void save_Success() {
        InstructorCreateDto createDto = new InstructorCreateDto("John", "Doe", "Desc", "Spec");

        when(instructorMapper.toEntity(createDto)).thenReturn(instructor);
        when(instructorRepository.save(instructor)).thenReturn(instructor);
        when(instructorMapper.toResponse(instructor)).thenReturn(responseDto);

        InstructorResponseDto result = instructorService.save(createDto);

        assertNotNull(result);
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void update_Success() {
        InstructorUpdateDto updateDto = new InstructorUpdateDto("New Desc", "New Spec", "John", "Doe");

        when(instructorRepository.findById(ID)).thenReturn(Optional.of(instructor));
        // Важно: настраиваем маппер, чтобы .map() в сервисе не вернул Optional.empty()
        when(instructorMapper.toResponse(instructor)).thenReturn(responseDto);

        InstructorResponseDto result = instructorService.update(updateDto, ID);

        assertNotNull(result);
        verify(instructorMapper).updateEntity(updateDto, instructor);
    }

    @Test
    void delete_Success() {
        when(instructorRepository.findById(ID)).thenReturn(Optional.of(instructor));

        instructorService.delete(ID);

        verify(instructorRepository).delete(instructor);
    }

    @Test
    void delete_NotFound_ThrowsException() {
        when(instructorRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(InstructorNotFoundException.class, () -> instructorService.delete(ID));
        verify(instructorRepository, never()).delete(any());
    }
}
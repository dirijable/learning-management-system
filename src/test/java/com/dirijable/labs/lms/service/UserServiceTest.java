package com.dirijable.labs.lms.service;

import com.dirijable.labs.lms.db.entity.User;
import com.dirijable.labs.lms.db.entity.UserRole;
import com.dirijable.labs.lms.db.repository.UserRepository;
import com.dirijable.labs.lms.dto.user.UserCreateDto;
import com.dirijable.labs.lms.dto.user.UserResponseDto;
import com.dirijable.labs.lms.dto.user.UserUpdateDto;
import com.dirijable.labs.lms.exception.business.conflict.EmailAlreadyExistException;
import com.dirijable.labs.lms.exception.business.notfound.InstructorNotFoundException;
import com.dirijable.labs.lms.mapper.UserMapper;
import com.dirijable.labs.lms.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Long ID = 1L;
    private User user;
    private UserResponseDto responseDto;
    private UserCreateDto createDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(ID)
                .email("test@mail.com")
                .password("raw_password")
                .role(UserRole.USER)
                .build();

        responseDto = new UserResponseDto(ID, "Ivan", "Ivanov", "test@mail.com", List.of());
        createDto = new UserCreateDto("Ivan", "Ivanov", "test@mail.com", "secure_password123");
    }

    @Test
    void findById_Success() {
        when(userRepository.findByIdOptimized(ID)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        UserResponseDto result = userService.findById(ID);

        assertNotNull(result);
        assertEquals(ID, result.id());
        verify(userRepository).findByIdOptimized(ID);
    }

    @Test
    void save_Success() {
        when(userRepository.existsByEmail(createDto.email())).thenReturn(false);
        when(userMapper.toEntity(createDto)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_pass");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        UserResponseDto result = userService.save(createDto);

        assertNotNull(result);
        verify(passwordEncoder).encode(createDto.password()); // Проверяем, что пароль шифровался
        verify(userRepository).save(any(User.class));
    }

    @Test
    void save_EmailExists_ThrowsException() {
        when(userRepository.existsByEmail(createDto.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistException.class, () -> userService.save(createDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_Success() {
        UserUpdateDto updateDto = new UserUpdateDto("New", "Name", "new@mail.com", "new_password_long");
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        userService.update(updateDto, ID);

        verify(userMapper).updateEntity(updateDto, user);
        verify(userRepository).save(user);
    }

    @Test
    void deleteById_Success() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        userService.deleteById(ID);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteById_NotFound_ThrowsException() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(InstructorNotFoundException.class, () -> userService.deleteById(ID));
    }
}
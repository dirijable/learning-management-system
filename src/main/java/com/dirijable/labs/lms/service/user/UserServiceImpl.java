package com.dirijable.labs.lms.service.user;

import com.dirijable.labs.lms.db.entity.User;
import com.dirijable.labs.lms.dto.user.UserCreateDto;
import com.dirijable.labs.lms.dto.user.UserResponseDto;
import com.dirijable.labs.lms.dto.user.UserUpdateDto;
import com.dirijable.labs.lms.exception.business.conflict.EmailAlreadyExistException;
import com.dirijable.labs.lms.exception.business.notfound.InstructorNotFoundException;
import com.dirijable.labs.lms.exception.business.notfound.UserNotFoundException;
import com.dirijable.labs.lms.mapper.UserMapper;
import com.dirijable.labs.lms.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto findById(Long id) {
    return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponseDto save(UserCreateDto createDto) {
        if (userRepository.existsByEmail(createDto.email())) {
            throw new EmailAlreadyExistException("Email %s already exist".formatted(createDto.email()));
        }
        User entity = userMapper.toEntity(createDto);
        entity.setPassword(passwordEncoder.encode(createDto.password()));
        User savedEntity = userRepository.save(entity);
        return userMapper.toResponse(savedEntity);
    }

    @Override
    public UserResponseDto update(UserUpdateDto createDto, Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    userMapper.updateEntity(createDto, user);
                    return userRepository.save(user);
                })
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
    }

    @Override
    public void deleteById(Long userId) {
        User instructor = userRepository.findById(userId)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor not found with id: " + userId));
        userRepository.delete(instructor);
    }
}

package com.dirijable.labs.lms.service.user;

import com.dirijable.labs.lms.dto.user.UserCreateDto;
import com.dirijable.labs.lms.dto.user.UserResponseDto;
import com.dirijable.labs.lms.dto.user.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserResponseDto findById(Long id);

    List<UserResponseDto> findAll();

    UserResponseDto save(UserCreateDto createDto);

    UserResponseDto update(UserUpdateDto createDto, Long userId);

    void deleteById(Long id);
}

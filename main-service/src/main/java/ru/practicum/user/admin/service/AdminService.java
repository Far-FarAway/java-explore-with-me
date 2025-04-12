package ru.practicum.user.admin.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto postUser(NewUserRequest user);

    void deleteUser(Long userId);

    CategoryDto postCategory(NewCategoryDto dto);

    void deleteCategory(Long catId);

    CategoryDto patchCategory(NewCategoryDto dto, Long catId);
}

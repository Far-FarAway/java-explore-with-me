package ru.practicum.user.admin.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.RequestCompilationDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.SearchProperties;

import java.util.List;

public interface AdminService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto postUser(NewUserRequest user);

    void deleteUser(Long userId);

    CategoryDto postCategory(NewCategoryDto dto);

    void deleteCategory(Long categoryId);

    CategoryDto patchCategory(NewCategoryDto dto, Long categoryId);

    CompilationDto postCompilation(RequestCompilationDto dto);

    void deleteCompilation(Long compilationId);

    CompilationDto patchCompilation(RequestCompilationDto dto, Long compilationId);

    EventFullDto patchEvent(NewEventDto dto, Long eventId);

    List<EventFullDto> getEvents(SearchProperties properties);

    CommentDto patchComment(NewCommentDto dto, Long commentId);

    void deleteComment(Long commentId);
}

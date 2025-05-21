package ru.practicum.user.admin.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.RequestCompilationDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.marker.OnCreate;
import ru.practicum.marker.OnUpdate;
import ru.practicum.user.admin.service.AdminService;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.SearchProperties;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    AdminService service;

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("get users with ids [{}], from {} index and with size {}", ids, from, size);
        return service.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@Validated(OnCreate.class) @RequestBody NewUserRequest user) {
        return service.postUser(user);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@Validated(OnCreate.class) @RequestBody NewCategoryDto dto) {
        log.info("Post category = {}", dto);
        return service.postCategory(dto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = "catId") Long categoryId) {
        service.deleteCategory(categoryId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto patchCategory(@Validated(OnUpdate.class) @RequestBody NewCategoryDto dto, @PathVariable(name = "catId") Long categoryId) {
        return service.patchCategory(dto, categoryId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@Validated(OnCreate.class) @RequestBody RequestCompilationDto dto) {
        return service.postCompilation(dto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(name = "compId") Long compilationId) {
        service.deleteCompilation(compilationId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto patchCompilation(@Validated(OnUpdate.class) @RequestBody RequestCompilationDto dto,
                                           @PathVariable(name = "compId") Long compilationId) {
        return service.patchCompilation(dto, compilationId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patchEvent(@Validated(OnUpdate.class) @RequestBody NewEventDto dto, @PathVariable Long eventId) {
        log.info("Patch event(id={}) with properties: {}", eventId, dto);
        return service.patchEvent(dto, eventId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                         @RequestParam(required = false) List<String> states,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        SearchProperties properties = SearchProperties.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        return service.getEvents(properties);
    }

    @PatchMapping("comments/{commentId}")
    public CommentDto patchComment(@Validated @RequestBody NewCommentDto dto,
                                   @PathVariable Long commentId) {
        return service.patchComment(dto, commentId);
    }

    @DeleteMapping("comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        service.deleteComment(commentId);
    }
}

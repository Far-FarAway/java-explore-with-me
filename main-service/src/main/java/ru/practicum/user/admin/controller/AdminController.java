package ru.practicum.user.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
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
public class AdminController {
    private final AdminService service;

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
    public void deleteCategory(@PathVariable Long catId) {
        service.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto patchCategory(@Validated(OnUpdate.class) @RequestBody NewCategoryDto dto, @PathVariable Long catId) {
        return service.patchCategory(dto, catId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@Validated(OnCreate.class) @RequestBody RequestCompilationDto dto) {
        return service.postCompilation(dto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        service.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto patchCompilation(@Validated(OnUpdate.class) @RequestBody RequestCompilationDto dto,
                                           @PathVariable Long compId) {
        return service.patchCompilation(dto, compId);
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
}

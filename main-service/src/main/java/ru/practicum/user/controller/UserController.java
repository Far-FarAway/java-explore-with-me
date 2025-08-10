package ru.practicum.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.marker.OnCreate;
import ru.practicum.marker.OnUpdate;
import ru.practicum.user.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService service;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        log.info("Получение запросов пользователя с id {}", userId);
        return service.getUserRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto postUserRequests(@PathVariable Long userId,
                                                    @RequestParam Long eventId) {
        log.info("post user(id={}) request to event(id={})", userId, eventId);
        return service.postUserRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto patchUserRequests(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {
        log.info("Обновление запроса с id {} пользователя с id {}", requestId, userId);
        return service.patchUserRequests(userId, requestId);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId, @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("Получение событий пользователя с id {}, from={}, size={}", userId, from, size);
        return service.getUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@Validated(OnCreate.class) @RequestBody NewEventDto dto, @PathVariable Long userId) {
        log.info("Сохранение события {} пользователя с id {}", dto, userId);
        return service.postEvent(dto, userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение события с id {} пользователя с id {}", eventId, userId);
        return service.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto patchUserEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                       @Validated(OnUpdate.class) @RequestBody NewEventDto dto) {
        log.info("Обновление события с id {} пользователя с id {} на {}", eventId, userId, dto);
        return service.patchUserEvent(userId, eventId, dto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение запросов на событие с id {} пользователя с id {}", eventId, userId);
        return service.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest dto) {
        log.info("Обновление запроса на событие с id {} пользователя с id {} на {}", eventId, userId, dto);
        return service.patchUserEventRequests(userId, eventId, dto);
    }

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@Validated @RequestBody NewCommentDto dto,
                                  @PathVariable Long userId) {
        log.info("Сохранение комментария {} пользователя с id {}", dto, userId);
        return service.postComment(dto, userId);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentDto patchComment(@Validated @RequestBody NewCommentDto dto,
                                   @PathVariable Long commentId,
                                   @PathVariable Long userId) {
        log.info("Обновление комментария с id {} пользователя с id {} на {}", commentId, userId, dto);
        return service.patchComment(dto, commentId, userId);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @PathVariable Long userId) {
        log.info("Удаление комментария с id {} пользователя с id {}", commentId, userId);
        service.deleteComment(commentId, userId);
    }
}

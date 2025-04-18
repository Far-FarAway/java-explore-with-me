package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.user.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        return service.getUserRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto postUserRequests(@PathVariable Long userId,
                                                    @RequestParam Long eventId) {
        return service.postUserRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto patchUserRequests(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {
        return service.patchUserRequests(userId, requestId);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId, @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return service.getUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto postEvent(@RequestBody NewEventDto dto, @PathVariable Long userId) {
        return service.postEvent(dto, userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto patchUserEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                       @RequestBody NewEventDto dto) {
        return service.patchUserEvent(userId, eventId, dto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult getUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest dto) {
        return service.patchUserEventRequests(userId, eventId, dto);
    }
}

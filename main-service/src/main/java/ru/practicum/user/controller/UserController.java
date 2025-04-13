package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
}

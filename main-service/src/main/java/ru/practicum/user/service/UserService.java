package ru.practicum.user.service;

import ru.practicum.user.dto.request.ParticipationRequestDto;

import java.util.List;

public interface UserService {
    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto postUserRequests(Long userId, Long eventId);

    ParticipationRequestDto patchUserRequests(Long userId, Long requestId);
}

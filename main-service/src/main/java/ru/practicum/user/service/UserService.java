package ru.practicum.user.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.user.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.user.dto.request.ParticipationRequestDto;

import java.util.List;

public interface UserService {
    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto postUserRequests(Long userId, Long eventId);

    ParticipationRequestDto patchUserRequests(Long userId, Long requestId);

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto postEvent(NewEventDto dto, Long userId);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto patchUserEvent(Long userId, Long eventId, NewEventDto dto);

    List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchUserEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest dto);
}

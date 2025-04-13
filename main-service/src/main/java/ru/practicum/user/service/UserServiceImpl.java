package ru.practicum.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.mapper.RequestMapper;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.ParticipationRequest;
import ru.practicum.user.model.RequestStatus;
import ru.practicum.user.repository.RequestRepository;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository repository;
    RequestRepository requestRepository;
    EventRepository eventRepository;
    UserMapper mapper;
    RequestMapper requestMapper;

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        return requestRepository.findByRequester(userId).stream()
                .map(requestMapper::mapDto)
                .toList();
    }


    @Override
    public ParticipationRequestDto postUserRequests(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        valid(event, eventId, userId);

        eventRepository.increaseConfirmRequests(eventId);

        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .eventId(eventId)
                .requester(userId)
                .build();

        if (event.isRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        return requestMapper.mapDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto patchUserRequests(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));

        request.setStatus(RequestStatus.REJECTED);
        eventRepository.decreaseConfirmRequests(request.getEventId());

        return requestMapper.mapDto(requestRepository.save(request));
    }

    private void valid(Event event, Long eventId, Long userId) {
        if (requestRepository.existsByEventIdAndRequester(eventId, userId)) {
            throw new ConflictException("Request with user id=" + userId + " already exists",
                    "Integrity constraint has been violated.");
        }

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("Initiator can't make request to own event",
                    "Integrity constraint has been violated.");
        }

        if (event.getPublishedOn() == null) {
            throw new ConflictException("Can't take part in not published event",
                    "Integrity constraint has been violated.");
        }

        if (event.getParticipantLimit() == event.getConfirmRequests()) {
            throw new ConflictException("Requests limit is overfull",
                    "Integrity constraint has been violated.");
        }

    }
}


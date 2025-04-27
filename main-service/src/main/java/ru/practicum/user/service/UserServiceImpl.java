package ru.practicum.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Client;
import ru.practicum.ResponseStatDto;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.StateAction;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConditionsNotMetException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.mapper.RequestMapper;
import ru.practicum.user.model.ParticipationRequest;
import ru.practicum.user.model.RequestStatus;
import ru.practicum.user.repository.RequestRepository;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    RequestRepository requestRepository;
    EventRepository eventRepository;
    CategoryRepository catRepository;
    UserRepository userRepository;
    RequestMapper requestMapper;
    EventMapper eventMapper;
    Client client;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .eventId(eventId)
                .requester(userId)
                .build();

        if (event.isRequestModeration() && event.getParticipantLimit() > 0) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
            eventRepository.increaseConfirmRequests(eventId);
        }

        return requestMapper.mapDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto patchUserRequests(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));

        request.setStatus(RequestStatus.CANCELED);
        eventRepository.decreaseConfirmRequests(request.getEventId());

        return requestMapper.mapDto(requestRepository.save(request));
    }


    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        List<Event> events = eventRepository.findByInitiator_Id(userId, pageable).stream()
                .toList();

        Map<String, Object> params = new HashMap<>();

        params.put("start", LocalDateTime.of(2000, 1, 1, 0, 0, 0)
                .format(formatter));
        params.put("end", LocalDateTime.of(3000, 1, 1, 0, 0, 0)
                .format(formatter));
        List<String> uris = new ArrayList<>();

        events.forEach(event -> uris.add("/events/" + event.getId()));
        params.put("uris", uris);

        List<ResponseStatDto> stats = client.getStats(params).getBody();
        Map<Long, Long> viewsStat = new HashMap<>();

        stats.forEach(stat -> {
            List<String> uri = Arrays.asList(stat.getUri().split("/"));
            viewsStat.put(Long.parseLong(uri.getLast()), stat.getHits());
        });

        return events.stream()
                .map(event -> {
                    Long views = 0L;

                    if (viewsStat.containsKey(event.getId())) {
                        views = viewsStat.get(event.getId());
                    }

                    return eventMapper.mapShortDto(event, views);
                })
                .toList();
    }

    @Override
    public EventFullDto postEvent(NewEventDto dto, Long userId) {
        LocalDateTime eventDate = LocalDateTime.parse(dto.getEventDate(), formatter);

        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Field: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила. Value: " + dto.getEventDate());
        }

        Event event = eventMapper.toEntity(dto);
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found")));
        event.setState(EventState.PENDING);

        return eventMapper.mapDto(eventRepository.save(event), 0L);
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        List<Optional<Event>> events = new ArrayList<>();
        List<List<ResponseStatDto>> stats = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();

        params.put("start", LocalDateTime.of(2000, 1, 1, 0, 0, 0)
                .format(formatter));
        params.put("end", LocalDateTime.of(3000, 1, 1, 0, 0, 0)
                .format(formatter));
        params.put("uris", "/events/" + eventId);

        Thread statThread = new Thread(() -> stats.add(client.getStats(params).getBody()));
        Thread mainThread = new Thread(() -> events.add(eventRepository.findById(eventId)));

        try {
            statThread.start();
            mainThread.start();
            statThread.join();
            mainThread.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Event event = events.getFirst()
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        return eventMapper.mapDto(event,
                !stats.getFirst().isEmpty() ? stats.getFirst().getFirst().getHits() : 0L);
    }

    @Override
    public EventFullDto patchUserEvent(Long userId, Long eventId, NewEventDto dto) {
        List<Event> events = new ArrayList<>();
        List<List<ResponseStatDto>> stats = new ArrayList<>();
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        LocalDateTime newEventDate = dto.getEventDate() != null ? LocalDateTime.parse(dto.getEventDate(), formatter) :
                null;

        if (newEventDate != null && newEventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Field: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила. Value: " + dto.getEventDate());
        }

        if (oldEvent.getState() != EventState.PENDING && oldEvent.getState() != EventState.CANCELED &&
                dto.getStateAction() != StateAction.SEND_TO_REVIEW) {
            throw new ConflictException("Only pending or canceled events can be changed",
                    "For the requested operation the conditions are not met.");
        }

        Event updatedEvent = oldEvent.toBuilder()
                .id(eventId)
                .annotation(dto.getAnnotation() != null ? dto.getAnnotation() : oldEvent.getAnnotation())
                .category(dto.getCategory() != null ? catRepository.findById(dto.getCategory()).orElseThrow(() ->
                        new NotFoundException("Category with id=" + dto.getCategory() + " was not found")) :
                        oldEvent.getCategory())
                .description(dto.getDescription() != null ? dto.getDescription() : oldEvent.getDescription())
                .eventDate(newEventDate != null ? newEventDate : oldEvent.getEventDate())
                .location(dto.getLocation() != null ? dto.getLocation() : oldEvent.getLocation())
                .paid(dto.getPaid() != null ? dto.getPaid() : oldEvent.isPaid())
                .participantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() :
                        oldEvent.getParticipantLimit())
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() :
                        oldEvent.isRequestModeration())
                .title(dto.getTitle() != null ? dto.getTitle() : oldEvent.getTitle())
                .build();

        if (dto.getStateAction() == StateAction.PUBLISH_EVENT) {
            updatedEvent.setState(EventState.PUBLISHED);
        } else if (dto.getStateAction() == StateAction.CANCEL_REVIEW) {
            updatedEvent.setState(EventState.CANCELED);
        } else if (dto.getStateAction() == StateAction.SEND_TO_REVIEW) {
            updatedEvent.setState(EventState.PENDING);
        }

        Thread statThread = new Thread(() -> {
            Map<String, Object> params = new HashMap<>();

            params.put("start", LocalDateTime.of(2000, 1, 1, 0, 0, 0)
                    .format(formatter));
            params.put("end", LocalDateTime.of(3000, 1, 1, 0, 0, 0)
                    .format(formatter));
            params.put("uris", "/events/" + eventId);

            stats.add(client.getStats(params).getBody());
        });
        Thread mainThread = new Thread(() -> events.add(eventRepository.save(updatedEvent)));

        try {
            statThread.start();
            mainThread.start();
            statThread.join();
            mainThread.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        return eventMapper.mapDto(events.getFirst(),
                !stats.getFirst().isEmpty() ? stats.getFirst().getFirst().getHits() : 0L);
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId) {
        return requestRepository.findByEventId(eventId).stream()
                .map(requestMapper::mapDto)
                .toList();
    }

    @Override
    public EventRequestStatusUpdateResult patchUserEventRequests(Long userId, Long eventId,
                                                                 EventRequestStatusUpdateRequest dto) {
        List<Long> ids = dto.getRequestIds();
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        boolean overLimit = false;

        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() - event.getConfirmRequests() < ids.size()) {
            ids = ids.stream()
                    .limit(event.getParticipantLimit() - event.getConfirmRequests())
                    .toList();
            overLimit = true;
        }

        for (Long id : ids) {
            ParticipationRequest request = requestRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Request with id=" + id + " was not found"));

            if (request.getStatus().equals(RequestStatus.PENDING)) {
                if (dto.getStatus().equals(RequestStatus.CONFIRMED)) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    eventRepository.increaseConfirmRequests(eventId);
                    requestRepository.save(request);
                    confirmed.add(requestMapper.mapDto(request));
                } else if (dto.getStatus().equals(RequestStatus.REJECTED)) {
                    request.setStatus(RequestStatus.REJECTED);
                    eventRepository.decreaseConfirmRequests(eventId);
                    requestRepository.save(request);
                    rejected.add(requestMapper.mapDto(request));
                }
            } else {
                throw new ConditionsNotMetException("You can patch only the PENDING requests, but request with id=" +
                        id + " is " + request.getStatus());
            }
        }

        if (overLimit) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "The participant limit has been reached");
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();
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

        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() == event.getConfirmRequests()) {
            throw new ConflictException("Requests limit is overfull",
                    "Integrity constraint has been violated.");
        }
    }
}


package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Client;
import ru.practicum.RequestStatDto;
import ru.practicum.ResponseStatDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.admin.repository.AdminRepository;
import ru.practicum.user.model.SearchProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceImpl implements EventsService {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    EventRepository repository;
    AdminRepository adminRepository;
    CommentRepository commentRepository;
    EventMapper mapper;
    CommentMapper commentMapper;
    Client client;

    @Override
    public List<EventShortDto> getEvents(SearchProperties properties, String ip) {
        Comparator<EventShortDto> viewsComparator = (o1, o2) -> o1.getViews().compareTo(o2.getViews());
        DateComparator dateComparator = new DateComparator();

        if (properties.getRangeStart() != null && !properties.getRangeStart().isEmpty() &&
                properties.getRangeEnd() != null && !properties.getRangeEnd().isEmpty()) {
            LocalDateTime start = LocalDateTime.parse(properties.getRangeStart(), formatter);
            LocalDateTime end = LocalDateTime.parse(properties.getRangeEnd(), formatter);
            if (end.getYear() - start.getYear() < 0) {
                throw new BadRequestException("Start date is after end date");
            }
        }

        BooleanBuilder predicates = new BooleanBuilder();
        Map<String, Object> params = new HashMap<>();

        prepareQueryToGetEvents(predicates, properties, params);

        List<Stream<Event>> eventStream = new ArrayList<>();
        List<List<ResponseStatDto>> statsList = new ArrayList<>();

        Thread clientThread = new Thread(() -> statsList.add(client.getStats(params).getBody()));

        Thread eventThread = new Thread(() -> {
            int page = properties.getFrom() / properties.getSize();
            Pageable pageable = PageRequest.of(page, properties.getSize());
            eventStream.add(StreamSupport.stream(repository.findAll(predicates, pageable).spliterator(), false));
        });

        try {
            clientThread.start();
            eventThread.start();
            clientThread.join();
            eventThread.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Map<Long, Long> stats = new HashMap<>();

        statsList.getFirst().forEach(event -> {
            List<String> uri = Arrays.asList(event.getUri().split("/"));
            stats.put(Long.parseLong(uri.getLast()), event.getHits());
        });

        List<EventShortDto> events = eventStream.getFirst()
                .map(event -> {
                    Long views = 0L;

                    if (stats.containsKey(event.getId())) {
                        views = stats.get(event.getId());
                    }

                    return mapper.mapShortDto(event, views);
                })
                .toList();

        events.forEach(event -> {
            RequestStatDto stat = RequestStatDto.builder()
                    .app("ewm-main-service")
                    .uri("/events/" + event.getId())
                    .ip(ip)
                    .timestamp(LocalDateTime.now().format(formatter))
                    .build();

            client.postStat(stat);
        });

        if (properties.getSort() != null && !properties.getSort().isEmpty()) {
            if (properties.getSort().equals(SortType.EVENT_DATE.name())) {
                return events.stream()
                        .sorted(dateComparator)
                        .toList();
            } else if (properties.getSort().equals(SortType.VIEWS.name())) {
                return events.stream()
                        .sorted(viewsComparator)
                        .toList();
            }
        }



        return events;
    }

    @Override
    public EventFullDto getEvent(Long id, String ip) {
        final List<List<ResponseStatDto>> stats = new ArrayList<>();
        final List<Optional<Event>> events = new ArrayList<>();

        RequestStatDto postStat = RequestStatDto.builder()
                .app("ewm-main-service")
                .uri("/events/" + id)
                .ip(ip)
                .timestamp(LocalDateTime.now().format(formatter))
                .build();

        client.postStat(postStat);

        Thread clientThread = new Thread(() -> {
            Map<String, Object> params = new HashMap<>();

            params.put("start", LocalDateTime
                    .of(2000, 1, 1, 0, 0).format(formatter));
            params.put("end", LocalDateTime.now().format(formatter));
            params.put("uris", "/events/" + id);
            params.put("unique", "true");

            stats.add(client.getStats(params).getBody());
        });

        Thread eventThread = new Thread(() -> events.add(repository.findById(id)));

        try {
            clientThread.start();
            eventThread.start();
            clientThread.join();
            eventThread.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Event event = events.getFirst()
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found"));

        if (event.getPublishedOn() == null) {
            throw new NotFoundException("Event must be published");
        }

        return mapper.mapDto(event,
                !stats.getFirst().isEmpty() ? stats.getFirst().getFirst().getHits() : 0L);
    }

    @Override
    public void prepareQueryToGetEvents(BooleanBuilder predicates, SearchProperties properties,
                                        Map<String, Object> params) {
        QEvent event = QEvent.event;

        if (properties.getUsers() != null && !properties.getUsers().isEmpty()) {
            predicates.and(event.initiator.in(adminRepository.findByIdIn(properties.getUsers())));
        }

        if (properties.getStates() != null && !properties.getStates().isEmpty()) {
            List<EventState> states = properties.getStates().stream().map(EventState::valueOf).toList();

            predicates.and(event.state.in(states));
        }

        if (properties.getText() != null && !properties.getText().isEmpty()) {
            predicates.and(event.annotation.likeIgnoreCase(properties.getText())
                    .or(event.description.likeIgnoreCase(properties.getText())));
        }

        if (properties.getCategories() != null && !properties.getCategories().isEmpty()) {
            predicates.and(event.category.id.in(properties.getCategories()));
        }

        if (properties.getPaid() != null) {
            predicates.and(event.paid.eq(properties.getPaid()));
        }

        if (properties.getRangeStart() != null && !properties.getRangeStart().isEmpty()) {
            predicates.and(event.eventDate.after(LocalDateTime.parse(properties.getRangeStart(), formatter)));
            params.put("start", properties.getRangeStart());
        } else {
            params.put("start", LocalDateTime
                    .of(2000, 1, 1, 0, 0).format(formatter));
        }

        if (properties.getRangeEnd() != null && !properties.getRangeEnd().isEmpty()) {
            predicates.and(event.eventDate.before(LocalDateTime.parse(properties.getRangeEnd(), formatter)));
            params.put("end", properties.getRangeEnd());
        } else {
            params.put("end", LocalDateTime.now().format(formatter));
        }

        if (properties.getOnlyAvailable() != null && properties.getOnlyAvailable()) {
            predicates.and(event.participantLimit.gt(event.confirmRequests).or(event.participantLimit.eq(0)));
        }
    }

    @Override
    public List<CommentDto> getComments(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Event with id=" + id + " was not found");
        }

        return commentRepository.findByEventId(id).stream()
                .map(commentMapper::mapDto)
                .toList();
    }
}
package ru.practicum.user.admin.service;

import com.querydsl.core.BooleanBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Client;
import ru.practicum.ResponseStatDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.RequestCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.StateAction;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventsService;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConditionsNotMetException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.admin.repository.AdminRepository;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.SearchProperties;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminServiceImpl implements AdminService {
    AdminRepository repository;
    CategoryRepository catRepository;
    EventRepository eventRepository;
    CompilationRepository compRepository;
    CommentRepository commentRepository;
    UserMapper mapper;
    CategoryMapper catMapper;
    CompilationMapper compMapper;
    EventMapper eventMapper;
    CommentMapper commentMapper;
    EventsService eventsService;
    Client client;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        if (ids != null && !ids.isEmpty()) {
            return repository.findByIdIn(ids, pageable).stream()
                    .map(mapper::mapDto)
                    .toList();
        } else {
            return repository.findAll(pageable).stream()
                    .map(mapper::mapDto)
                    .toList();
        }
    }

    @Override
    public UserDto postUser(NewUserRequest user) {
        return mapper.mapDto(repository.save(mapper.toEntity(user)));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        repository.deleteById(userId);
    }

    @Override
    public CategoryDto postCategory(NewCategoryDto dto) {
        return catMapper.mapDto(catRepository.save(catMapper.toEntity(dto)));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!catRepository.existsById(categoryId)) {
            throw new NotFoundException("Category with id=" + categoryId + " was not found");
        }

        if (eventRepository.existsByCategory_Id(categoryId)) {
            throw new ConflictException("The category is not empty",
                    "For the requested operation the conditions are not met.");
        }

        catRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto patchCategory(NewCategoryDto dto, Long categoryId) {
        Category category = catRepository.findById(categoryId)
                .orElseThrow(() ->  new NotFoundException("Category with id=" + categoryId + " was not found"));

        category.setName(dto.getName());

        return catMapper.mapDto(catRepository.save(category));
    }

    @Override
    public CompilationDto postCompilation(RequestCompilationDto dto) {
        List<Event> events = eventRepository.findByIdIn(dto.getEvents());

        return compMapper.mapDto(compRepository.save(compMapper.toEntity(dto, events)));
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        compRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto patchCompilation(RequestCompilationDto dto, Long compilationId) {
        Compilation oldComp = compRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compilationId + " was not found"));

        Compilation comp = oldComp.toBuilder()
                .pinned(dto.isPinned())
                .title(dto.getTitle() != null ? dto.getTitle() : oldComp.getTitle())
                .events(eventRepository.findByIdIn(dto.getEvents()))
                .build();

        return compMapper.mapDto(compRepository.save(comp));
    }

    @Override
    public EventFullDto patchEvent(NewEventDto dto, Long eventId) {
        if (dto.getEventDate() != null && !dto.getEventDate().isEmpty() &&
                LocalDateTime.parse(dto.getEventDate(), formatter)
                        .isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Event date before now");
        }

        List<Optional<Event>> event = new ArrayList<>();
        List<List<ResponseStatDto>> stats = new ArrayList<>();

        Thread mainThread = new Thread(() -> {
            event.add(eventRepository.findById(eventId));
        });

        Thread statThread = new Thread(() -> {
            Map<String, Object> params = new HashMap<>();

            params.put("start", LocalDateTime.of(2000, 1, 1, 0, 0, 0)
                    .format(formatter));
            params.put("end", LocalDateTime.of(3000, 1, 1, 0, 0, 0)
                    .format(formatter));
            params.put("uris", "/events/" + eventId);

            stats.add(client.getStats(params).getBody());
        });

        try {
            mainThread.start();
            statThread.start();
            mainThread.join();
            statThread.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Event oldEvent = event.getFirst()
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        Event updatedEvent = Event.builder()
                .eventDate(dto.getEventDate() != null ? LocalDateTime.parse(dto.getEventDate(), formatter) :
                        oldEvent.getEventDate())
                .build();

        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case StateAction.PUBLISH_EVENT -> {
                    if (updatedEvent.getEventDate().plusHours(1)
                            .isBefore(LocalDateTime.now())) {
                        throw new ConditionsNotMetException("Publish date must be after event date");
                    }

                    if (oldEvent.getState() == EventState.PENDING) {
                        updatedEvent.setState(EventState.PUBLISHED);
                        updatedEvent.setPublishedOn(LocalDateTime.now());
                    } else {
                        throw new ConflictException("Cannot publish the event because it's not in the right state: "
                                + oldEvent.getState(), "For the requested operation the conditions are not met.");
                    }
                }

                case StateAction.CANCEL_REVIEW -> {
                    if (oldEvent.getState() != EventState.PUBLISHED) {
                        updatedEvent.setState(EventState.CANCELED);
                    } else {
                        throw new ConflictException("Cannot cancel the event because it's not in the right state: "
                                + oldEvent.getState(), "For the requested operation the conditions are not met.");
                    }
                }

                case StateAction.REJECT_EVENT -> {
                    if (oldEvent.getState() != EventState.PUBLISHED) {
                        updatedEvent.setState(EventState.REJECTED);
                    } else {
                        throw new ConflictException("Cannot reject the event because it's not in the right state: "
                                + oldEvent.getState(), "For the requested operation the conditions are not met.");
                    }
                }
            }
        }

        updatedEvent = oldEvent.toBuilder()
                .state(updatedEvent.getState())
                .publishedOn(updatedEvent.getPublishedOn())
                .id(eventId)
                .annotation(dto.getAnnotation() != null ? dto.getAnnotation() : oldEvent.getAnnotation())
                .category(dto.getCategory() == null ? oldEvent.getCategory() :
                        catRepository.findById(dto.getCategory()).orElseThrow(() ->
                                new NotFoundException("Category with id=" + dto.getCategory() + " was not found")))
                .description(dto.getDescription() != null ? dto.getDescription() : oldEvent.getDescription())
                .eventDate(updatedEvent.getEventDate())
                .paid(dto.getPaid() != null ? dto.getPaid() : oldEvent.isPaid())
                .participantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() :
                        oldEvent.getParticipantLimit())
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() :
                        oldEvent.isRequestModeration())
                .title(dto.getTitle() != null ? dto.getTitle() : oldEvent.getTitle())
                .build();

        return eventMapper.mapDto(eventRepository.save(updatedEvent),
                !stats.getFirst().isEmpty() ? stats.getFirst().getFirst().getHits() : 0L);
    }

    @Override
    public List<EventFullDto> getEvents(SearchProperties properties) {
        BooleanBuilder predicates = new BooleanBuilder();
        Map<String, Object> params = new HashMap<>();

        eventsService.prepareQueryToGetEvents(predicates, properties, params);

        List<Stream<Event>> eventStream = new ArrayList<>();
        List<List<ResponseStatDto>> statsList = new ArrayList<>();

        Thread mainThread = new Thread(() -> {
            int page = properties.getFrom() / properties.getSize();
            Pageable pageable = PageRequest.of(page, properties.getSize());
            eventStream.add(StreamSupport.stream(eventRepository.findAll(predicates).spliterator(), false));
        });

        Thread statThread = new Thread(() -> statsList.add(client.getStats(params).getBody()));

        try {
            mainThread.start();
            statThread.start();
            mainThread.join();
            statThread.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Map<Long, Long> stats = new HashMap<>();

        statsList.getFirst().forEach(event -> {
            List<String> uri = Arrays.asList(event.getUri().split("/"));
            stats.put(Long.parseLong(uri.getLast()), event.getHits());
        });

        return eventStream.getFirst()
                .map(event -> {
                    Long views = 0L;

                    if (stats.containsKey(event.getId())) {
                        views = stats.get(event.getId());
                    }

                    return eventMapper.mapDto(event, views);
                })
                .skip(properties.getFrom())
                .limit(properties.getSize())
                .toList();
    }

    @Override
    public CommentDto patchComment(NewCommentDto dto, Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment with id=" + commentId + "was not found");
        }

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new NotFoundException("Event with id=" + dto.getEventId() + "was not found"));

        User user = repository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User with id=" + dto.getUserId() + "was not found"));

        Comment updatedComment = commentMapper.toEntity(dto, event, user);
        updatedComment.setId(commentId);

        return commentMapper.mapDto(commentRepository.save(updatedComment));
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment with id=" + commentId + "was not found");
        }

        commentRepository.deleteById(commentId);
    }
}

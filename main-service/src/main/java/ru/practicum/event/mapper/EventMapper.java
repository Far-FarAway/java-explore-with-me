package ru.practicum.event.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.InternalServerException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventMapper {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    UserMapper userMapper;
    CategoryMapper categoryMapper;
    CategoryRepository categoryRepository;

    public EventFullDto mapDto(Event event, Long views) {
        if (event == null) {
            throw new InternalServerException("Event is null");
        }

        if (event.getAnnotation().isEmpty() || event.getAnnotation().isBlank()) {
            throw new InternalServerException("Event annotation is null or blank");
        }

        if (event.getCategory() == null) {
            throw new InternalServerException("Event category is null");
        }

        if (event.getConfirmRequests() < 0) {
            throw new InternalServerException("Event confirmedRequests less than zero");
        }

        if (event.getCreatedOn() == null) {
            throw new InternalServerException("Event createdOn is null");
        }

        if (event.getDescription().isEmpty() || event.getDescription().isBlank()) {
            throw new InternalServerException("Event description is null or blank");
        }

        if (event.getEventDate() == null) {
            throw new InternalServerException("Event eventDate is null");
        }

        if (event.getInitiator() == null) {
            throw new InternalServerException("Event initiator is null");
        }

        if (event.getLocation() == null) {
            throw new InternalServerException("Event location is null");
        }

        if (event.getParticipantLimit() < 0) {
            throw new InternalServerException("Event ParticipantLimit less than zero");
        }

        if (event.getId() == null) {
            throw new InternalServerException("Event id is null");
        }

        if (views < 0) {
            throw new InternalServerException("Event views less than zero");
        }

        if (event.getTitle().isEmpty() || event.getTitle().isBlank()) {
            throw new InternalServerException("Event title is null or blank");
        }

        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.mapDto(event.getCategory()))
                .confirmedRequests(event.getConfirmRequests())
                .createdOn(event.getCreatedOn().format(formatter))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .publishedOn(event.getPublishedOn() != null ? event.getPublishedOn().format(formatter) : null)
                .initiator(userMapper.mapShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .id(event.getId())
                .state(event.getState())
                .views(views)
                .requestModeration(event.isRequestModeration())
                .title(event.getTitle())
                .build();
    }

    public EventShortDto mapShortDto(Event event, Long views) {
        if (event == null) {
            throw new InternalServerException("Event is null");
        }

        if (event.getAnnotation().isEmpty() || event.getAnnotation().isBlank()) {
            throw new InternalServerException("Event annotation is null or blank");
        }

        if (event.getCategory() == null) {
            throw new InternalServerException("Event category is null");
        }

        if (event.getEventDate() == null) {
            throw new InternalServerException("Event eventDate is null");
        }

        if (event.getInitiator() == null) {
            throw new InternalServerException("Event initiator is null");
        }

        if (event.getId() == null) {
            throw new InternalServerException("Event id is null");
        }

        if (views < 0) {
            throw new InternalServerException("Event views less than zero");
        }

        if (event.getTitle().isEmpty() || event.getTitle().isBlank()) {
            throw new InternalServerException("Event title is null or blank");
        }

        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.mapDto(event.getCategory()))
                .eventDate(event.getEventDate().format(formatter))
                .initiator(userMapper.mapShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .id(event.getId())
                .views(views)
                .title(event.getTitle())
                .build();
    }

    public Event toEntity(NewEventDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .category(categoryRepository.findById(dto.getCategory()).orElseThrow(() ->
                        new NotFoundException("Category with id=" + dto.getCategory() + " was not found")))
                .description(dto.getDescription())
                .eventDate(LocalDateTime.parse(dto.getEventDate(), formatter))
                .location(dto.getLocation())
                .participantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() : 0)
                .paid(dto.getPaid() != null ? dto.getPaid() : false)
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() : true)
                .title(dto.getTitle())
                .build();
    }
}

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

    public Event mapPOJO(NewEventDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .category(categoryRepository.findById(dto.getCategory()).orElseThrow(() ->
                        new NotFoundException("Category with id=" + dto.getCategory() + " was not found")))
                .description(dto.getDescription())
                .eventDate(LocalDateTime.parse(dto.getEventDate(), formatter))
                .location(dto.getLocation())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .build();
    }
}

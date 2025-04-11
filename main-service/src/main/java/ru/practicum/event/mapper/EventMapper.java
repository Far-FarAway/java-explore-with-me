package ru.practicum.event.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public EventMapper(@Autowired UserMapper user, @Autowired CategoryMapper category) {
        this.userMapper = user;
        this.categoryMapper = category;
    }

    public EventFullDto mapDto(Event event, Long views) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.mapDto(event.getCategory()))
                .confirmRequests(event.getConfirmRequests())
                .createdOn(event.getCreatedOn().format(formatter))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .publishedOn(event.getPublishedOn().format(formatter))
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
                .category(categoryMapper.mapPOJO(dto.getCategory()))
                .description(dto.getDescription())
                .eventDate(LocalDateTime.parse(dto.getEventDate(), formatter))
                .location(dto.getLocation())
                .paid(dto.isPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.isRequestModeration())
                .title(dto.getTitle())
                .build();
    }
}

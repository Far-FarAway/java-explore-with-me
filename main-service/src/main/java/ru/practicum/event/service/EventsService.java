package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.model.SearchProperties;

import java.util.List;
import java.util.Map;

public interface EventsService {
    List<EventShortDto> getEvents(SearchProperties properties, String ip);

    EventFullDto getEvent(Long id, String ip);

    void prepareQueryToGetEvents(BooleanBuilder predicates, SearchProperties properties, Map<String, Object> params);

    List<CommentDto> getComments(Long id);
}

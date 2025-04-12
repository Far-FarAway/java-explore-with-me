package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.model.SearchProperties;

import java.util.List;

public interface EventsService {
    List<EventShortDto> getEvents(SearchProperties properties);

    EventFullDto getEvent(Long id);
}

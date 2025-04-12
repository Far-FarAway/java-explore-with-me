package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.Client;
import ru.practicum.ResponseStatDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.SearchProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class  EventServiceImpl implements EventsService {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    EventRepository repository;
    EventMapper mapper;
    Client client;



    @Override
    public EventFullDto getEvent(Long id) {
        Map<String, Object> params = new HashMap<>();

        params.put("start", LocalDateTime.of(2000, 1, 1, 0, 0).format(formatter));
        params.put("end", LocalDateTime.now().format(formatter));
        params.put("uris", Collections.singletonList(id));

        ResponseEntity<ResponseStatDto> stat = client.getStats(params);

        Event event = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found"));

        if (event.getPublishedOn() == null) {
            throw new NotFoundException("Event with id=" + id + " was not found");
        }

        return mapper.mapDto(event, stat.getBody().getHits());
    }
}

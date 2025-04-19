package ru.practicum.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.Client;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.RequestCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventMapper eventMapper;
    private final Client client;

    public Compilation mapPOJO(RequestCompilationDto dto, List<Event> events) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.isPinned())
                .events(events)
                .build();
    }

    public CompilationDto mapDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(getEventsWithViews(compilation))
                .build();
    }

    private List<EventShortDto> getEventsWithViews(Compilation compilation) {
        Map<String, Object> params = new HashMap<>();

        params.put("start", LocalDateTime.of(2000, 1, 1, 0, 0, 0)
                .format(formatter));
        params.put("end", LocalDateTime.of(3000, 1, 1, 0, 0, 0)
                .format(formatter));

        List<Long> eventsIds = compilation.getEvents().stream().map(Event::getId).toList();

        List<String> uris = new ArrayList<>();
        for (Long id : eventsIds) {
            uris.add("/events/" + id);
        }

        params.put("uris", uris);

        Map<Long, Long> stats = new HashMap<>();

        client.getStats(params).getBody().forEach(stat -> {
            List<String> uri = List.of(stat.getUri().split("/"));
            stats.put(Long.parseLong(uri.getLast()), stat.getHits());
        });

        return compilation.getEvents().stream()
                .map(event -> {
                    Long views = 0L;

                    if (stats.containsKey(event.getId())) {
                        views = stats.get(event.getId());
                    }

                    return eventMapper.mapShortDto(event, views);
                })
                .toList();
    }
}

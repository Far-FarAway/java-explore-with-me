package ru.practicum.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.RequestCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.Event;

import java.util.Set;

@Component
public class CompilationMapper {
    public Compilation mapPOJO(RequestCompilationDto dto, Set<Event> events) {
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
                .events(compilation.getEvents())
                .build();
    }
}

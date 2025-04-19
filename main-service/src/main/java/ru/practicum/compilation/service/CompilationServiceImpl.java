package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> results;

        if (pinned != null) {
            results = repository.findByPinned(pinned);
        } else {
            results = repository.findAll();
        }

        return results.stream()
                .skip(from)
                .limit(size)
                .map(mapper::mapDto)
                .toList();
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation comp = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        return mapper.mapDto(comp);
    }
}

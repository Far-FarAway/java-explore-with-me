package ru.practicum.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationServiceImpl implements CompilationService {
    CompilationRepository repository;
    CompilationMapper mapper;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Page<Compilation> results;
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        if (pinned != null) {
            results = repository.findByPinned(pinned, pageable);
        } else {
            results = repository.findAll(pageable);
        }

        return results.stream()
                .map(mapper::mapDto)
                .toList();
    }

    @Override
    public CompilationDto getCompilation(Long compilationId) {
        Compilation comp = repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compilationId + " was not found"));

        return mapper.mapDto(comp);
    }
}

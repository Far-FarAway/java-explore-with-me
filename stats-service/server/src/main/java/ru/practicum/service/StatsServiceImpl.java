package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.RequestStatDto;
import ru.practicum.ResponseStatDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    private final StatsMapper mapper;

    @Override
    public void postStat(RequestStatDto dto) {
        repository.save(mapper.mapPOJO(dto));
    }

    @Override
    public List<ResponseStatDto> getStats(String st, String en, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start = LocalDateTime.parse(st, formatter);
        LocalDateTime end = LocalDateTime.parse(en, formatter);

        if (uris != null && !uris.isEmpty()) {
            return repository.getStatsByUris(start, end, uris).stream()
                    .map(stat -> {
                        Long hits;
                        if (unique) {
                            hits = repository.findUniqueHitsByUrl(stat.getUri());
                        } else {
                            hits = repository.findHitsByUrl(stat.getUri());
                        }
                        return mapper.mapDto(stat, hits);
                    })
                    .distinct()
                    .toList();
        } else {
            return repository.getStats(start, end).stream()
                    .map(stat -> {
                        Long hits = repository.findHitsByUrl(stat.getUri());
                        return mapper.mapDto(stat, hits);
                    })
                    .distinct()
                    .toList();
        }
    }
}

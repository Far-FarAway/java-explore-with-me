package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.RequestStatDto;
import ru.practicum.ResponseStatDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
        Comparator<ResponseStatDto> comparator = (o1, o2) -> {
            if (o1.getHits() > o2.getHits()) {
                return 3;
            } else if (o1.getHits() < o2.getHits()) {
                return -3;
            } else {
                return 0;
            }
        };

        LocalDateTime start = LocalDateTime.parse(st, formatter);
        LocalDateTime end = LocalDateTime.parse(en, formatter);

        if (start.isAfter(end)) {
            throw new BadRequestException("Start after end");
        }

        if (uris != null && !uris.isEmpty()) {
            if (uris.getFirst().split("/").length == 2) {
                return repository.getStatsByUris(start, end, uris.getFirst()).stream()
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
                        .sorted(comparator)
                        .toList()
                        .reversed();
            } else {
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
                        .sorted(comparator)
                        .toList()
                        .reversed();
            }
        } else {
            return repository.getStats(start, end).stream()
                    .map(stat -> {
                        Long hits = repository.findHitsByUrl(stat.getUri());
                        return mapper.mapDto(stat, hits);
                    })
                    .distinct()
                    .sorted(comparator)
                    .toList()
                    .reversed();
        }
    }
}

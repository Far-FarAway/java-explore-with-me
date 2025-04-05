package ru.practicum.service;

import ru.practicum.RequestStatDto;
import ru.practicum.ResponseStatDto;

import java.util.List;

public interface StatsService {
    void postStat(RequestStatDto dto);

    List<ResponseStatDto> getStats(String start, String end, List<String> uris, Boolean unique);
}

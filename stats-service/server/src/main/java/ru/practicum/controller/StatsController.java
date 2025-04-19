package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.RequestStatDto;
import ru.practicum.ResponseStatDto;
import ru.practicum.service.StatsService;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void postStat(@RequestBody RequestStatDto dto) {
        log.info("Post stat - {}", dto);
        service.postStat(dto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseStatDto> getStats(@RequestParam String start, @RequestParam String end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam(defaultValue = "true") Boolean unique) {
        log.info("Get stat between date {} and {}, unique = {}, uris: {}", start, end, unique, uris);
        return service.getStats(start, end, uris, unique);
    }
}

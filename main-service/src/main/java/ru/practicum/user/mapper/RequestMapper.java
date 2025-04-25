package ru.practicum.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.model.ParticipationRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RequestMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ParticipationRequestDto mapDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated().format(formatter))
                .id(request.getId())
                .requester(request.getRequester())
                .event(request.getEventId())
                .status(request.getStatus())
                .build();
    }

    public ParticipationRequest toEntity(ParticipationRequestDto dto) {
        return ParticipationRequest.builder()
                .created(LocalDateTime.parse(dto.getCreated(), formatter))
                .id(dto.getId())
                .requester(dto.getRequester())
                .eventId(dto.getEvent())
                .status(dto.getStatus())
                .build();
    }
}

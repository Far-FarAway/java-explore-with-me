package ru.practicum.user.mapper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.InternalServerException;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.model.ParticipationRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestMapper {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ParticipationRequestDto mapDto(ParticipationRequest request) {
        if (request == null) {
            throw new InternalServerException("Request is null");
        }

        if (request.getCreated() == null) {
            throw new InternalServerException("Request created is null");
        }

        if (request.getRequester() == null) {
            throw new InternalServerException("Request requesterId is null");
        }

        if (request.getEventId() == null) {
            throw new InternalServerException("Request eventId is null");
        }

        if (request.getStatus() == null) {
            throw new InternalServerException("Request status is null");
        }

        return ParticipationRequestDto.builder()
                .created(request.getCreated().format(formatter))
                .id(request.getId())
                .requester(request.getRequester())
                .event(request.getEventId())
                .status(request.getStatus())
                .build();
    }

    public ParticipationRequest toEntity(ParticipationRequestDto dto) {
        if (dto.getRequester() == null) {
            throw new ConflictException("Request requesterId is null", "Conflict with class field");
        }

        if (dto.getEvent() == null) {
            throw new ConflictException("Request eventId is null", "Conflict with class field");
        }

        return ParticipationRequest.builder()
                .created(LocalDateTime.parse(dto.getCreated(), formatter))
                .id(dto.getId())
                .requester(dto.getRequester())
                .eventId(dto.getEvent())
                .status(dto.getStatus())
                .build();
    }
}

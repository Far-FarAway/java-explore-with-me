package ru.practicum.comment.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentMapper {
    EventRepository eventRepository;
    UserRepository userRepository;

    public Comment toEntity(NewCommentDto dto) {
        return Comment.builder()
                .event(eventRepository.findById(dto.getEventId())
                        .orElseThrow(() -> new NotFoundException("Event with id=" + dto.getEventId() + "was not found")))
                .user(userRepository.findById(dto.getUserId())
                        .orElseThrow(() -> new NotFoundException("User with id=" + dto.getUserId() + "was not found")))
                .text(dto.getText())
                .build();
    }

    public CommentDto mapDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .userId(comment.getUser().getId())
                .text(comment.getText())
                .build();
    }
}

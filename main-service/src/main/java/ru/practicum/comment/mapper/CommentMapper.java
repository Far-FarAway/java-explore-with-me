package ru.practicum.comment.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.InternalServerException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentMapper {
    EventRepository eventRepository;
    UserRepository userRepository;

    public Comment toEntity(NewCommentDto dto, Event event, User user) {
        if (dto.getText().isEmpty() || dto.getText().isBlank()) {
            throw new ConflictException("Comment text is null or blank", "Conflict with class field");
        }

        if (event == null) {
            throw new ConflictException("Comment event is null or blank", "Conflict with class field");
        }

        if (user == null) {
            throw new ConflictException("Comment user is null or blank", "Conflict with class field");
        }

        return Comment.builder()
                .event(event)
                .user(user)
                .text(dto.getText())
                .build();
    }

    public CommentDto mapDto(Comment comment) {
        if (comment == null) {
            throw new InternalServerException("Comment is null");
        }

        if (comment.getId() == null) {
            throw new InternalServerException("Comment id is null");
        }

        if (comment.getEvent() == null) {
            throw new InternalServerException("Comment event is null");
        }

        if (comment.getUser() == null) {
            throw new InternalServerException("Comment user is null");
        }

        if (comment.getText().isEmpty() || comment.getText().isBlank()) {
            throw new InternalServerException("Comment text is null or blank");
        }

        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .userId(comment.getUser().getId())
                .text(comment.getText())
                .build();
    }
}

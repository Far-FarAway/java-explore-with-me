package ru.practicum.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import ru.practicum.Client;
import ru.practicum.category.model.Category;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.user.mapper.RequestMapper;
import ru.practicum.user.model.ParticipationRequest;
import ru.practicum.user.model.RequestStatus;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Mock
    RequestRepository requestRepository;
    @Mock
    EventRepository eventRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    RequestMapper requestMapper;
    @Mock
    CommentMapper commentMapper;
    @Mock
    EventMapper eventMapper;
    @Mock
    Client client;
    @InjectMocks
    @Autowired
    UserServiceImpl service;

    @Test
    void testPatchUserRequest() {
        ParticipationRequest request = ParticipationRequest.builder()
                .requester(3L)
                .id(33L)
                .eventId(333L)
                .build();

        ParticipationRequest finalRequest = ParticipationRequest.builder()
                .requester(request.getRequester())
                .id(request.getId())
                .eventId(request.getEventId())
                .status(RequestStatus.CANCELED)
                .build();

        Mockito.when(requestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(request));

        service.patchUserRequests(request.getRequester(), request.getId());

        Mockito.verify(requestRepository).save(finalRequest);
    }

    @Test
    void testPatchUserEvent() {
        Event event = Event.builder()
                .id(3343L)
                .eventDate(LocalDateTime.now())
                .title("dadadadd")
                .description("54rt3eggreteghtrghrtgshsertyh")
                .category(Category.builder()
                        .name("TinkyWinky")
                        .build())
                .state(EventState.PENDING)
                .build();

        NewEventDto dto = NewEventDto.builder()
                .eventDate(LocalDateTime.now().plusDays(33).format(formatter))
                .annotation("sfsdfsdfsdfsdfsdfsdfsdfsdf")
                .description("dtghdthgsdgfhdghsfdgfhsdgfsdfgs4324324234")
                .title("turitotor")
                .build();

        Event finalEvent = Event.builder()
                .id(event.getId())
                .eventDate(LocalDateTime.parse(dto.getEventDate(), formatter))
                .title(dto.getTitle())
                .description(dto.getDescription())
                .annotation(dto.getAnnotation())
                .category(event.getCategory())
                .state(EventState.PENDING)
                .build();

        Mockito.when(eventRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(event));

        Mockito.when(client.getStats(Mockito.anyMap()))
                .thenReturn(ResponseEntity.of(Optional.of(List.of())));

        event.setCreatedOn(null);
        finalEvent.setCreatedOn(null);

        service.patchUserEvent(3L, event.getId(), dto);

        Mockito.verify(eventRepository).save(finalEvent);
    }

    @Test
    void testDeleteCommentWithWrongUserId() {
        Comment comm = Comment.builder()
                .id(43L)
                .user(User.builder().id(3L).build())
                .event(Event.builder().build())
                .text("null")
                .build();

        Mockito.when(commentRepository.findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(comm));

        assertThrows(ConflictException.class, () -> service.deleteComment(comm.getId(), 33L));
    }
}
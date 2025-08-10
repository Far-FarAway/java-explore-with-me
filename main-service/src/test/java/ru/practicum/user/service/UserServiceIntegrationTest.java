package ru.practicum.user.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.model.ParticipationRequest;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.RequestRepository;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {
    @Autowired
    UserService service;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository catRepository;
    @Autowired
    RequestRepository requestRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void testPostAndGetUserRequest() {
        User user = User.builder()
                .name("Tutoriel")
                .email("sdfgvkbhjsf@gmail.com")
                .build();

        Category cat = Category.builder()
                .name("TinkyWinky")
                .build();

        Event event = Event.builder()
                .initiator(user)
                .eventDate(LocalDateTime.now())
                .title("dadadadd")
                .description("54rt3eggreteghtrghrtgshsertyh")
                .category(cat)
                .state(EventState.PENDING)
                .build();

        userRepository.save(user);
        catRepository.save(cat);
        eventRepository.save(event);

        service.postUserRequests(user.getId(), event.getId());

        List<ParticipationRequestDto> result = service.getUserRequests(user.getId());

        assertThat(result.getFirst().getRequester(), is(user.getId()));
        assertThat(result.getFirst().getEvent(), is(event.getId()));
    }

    @Test
    void testPostAndGetUserEvent() {
        User user = User.builder()
                .name("Tutoriel")
                .email("sdfgvkbhjsf@gmail.com")
                .build();

        Category cat = Category.builder()
                .name("TinkyWinky")
                .build();

        NewEventDto dto = NewEventDto.builder()
                .eventDate(LocalDateTime.now().plusDays(30).format(formatter))
                .title("dadadadd")
                .description("54rt3eggreteghtrghrtgshsertyh")
                .category(cat.getId())
                .build();

        userRepository.save(user);
        catRepository.save(cat);

        EventFullDto result = service.postEvent(dto, user.getId());

        assertThat(result.getInitiator(), is(user.getId()));
        assertThat(result.getTitle(), is(dto.getTitle()));
    }

    @Test
    void testPostComment() {
        User user = User.builder()
                .name("Tutoriel")
                .email("sdfgvkbhjsf@gmail.com")
                .build();

        Category cat = Category.builder()
                .name("TinkyWinky")
                .build();

        Event event = Event.builder()
                .initiator(user)
                .eventDate(LocalDateTime.now())
                .title("dadadadd")
                .description("54rt3eggreteghtrghrtgshsertyh")
                .category(cat)
                .state(EventState.PENDING)
                .build();

        userRepository.save(user);
        catRepository.save(cat);
        eventRepository.save(event);

        NewCommentDto dto = NewCommentDto.builder()
                .eventId(event.getId())
                .text("jlksdfghjnoldjkgsoaldikfhjngoiusdhoiusjdfhgnodollszdjfgnbhkolkzsjdxfgfj")
                .build();

        CommentDto result = service.postComment(dto, user.getId());

        assertThat(result.getUserId(), is(user.getId()));
        assertThat(result.getText(), is(dto.getText()));
    }

    @Test
    void testGetUserEventRequest() {
        User owner = User.builder()
                .name("Tutoriel")
                .email("sdfgvkbhjsf@gmail.com")
                .build();

        User user1 = User.builder()
                .name("Tutoriel3232")
                .email("2223123@gmail.com")
                .build();

        User user2 = User.builder()
                .name("Tutoriel123123")
                .email("sdfgvkb231332hjsf@gmail.com")
                .build();

        Category cat = Category.builder()
                .name("TinkyWinky")
                .build();

        Event event = Event.builder()
                .initiator(owner)
                .eventDate(LocalDateTime.now())
                .title("dadadadd")
                .description("54rt3eggreteghtrghrtgshsertyh")
                .category(cat)
                .state(EventState.PENDING)
                .build();

        userRepository.save(owner);
        userRepository.save(user1);
        userRepository.save(user2);
        catRepository.save(cat);
        eventRepository.save(event);

        ParticipationRequest request1 = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .eventId(event.getId())
                .requester(user1.getId())
                .build();

        ParticipationRequest request2 = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .eventId(event.getId())
                .requester(user2.getId())
                .build();

        requestRepository.save(request1);
        requestRepository.save(request2);

        List<ParticipationRequestDto> result = service.getUserEventRequests(owner.getId(), event.getId());

        assertThat(result.size(), is(2));
        assertThat(result.getFirst().getRequester(), is(user1.getId()));
        assertThat(result.getFirst().getEvent(), is(event.getId()));
    }
}
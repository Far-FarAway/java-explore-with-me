package ru.practicum.user.admin.service;

import io.micrometer.core.instrument.search.Search;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.RequestCompilationDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.SearchProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AdminServiceIntegrationTest {
    @Autowired
    AdminService service;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void testPostAndGetUser() {
       NewUserRequest userDto1 = NewUserRequest.builder()
                .email("liksajhdfigjbi@gmail.com")
                .name("Турбулен")
                .build();

        NewUserRequest userDto2 = NewUserRequest.builder()
                .email("321123@gmail.com")
                .name("Gorechavka")
                .build();

        Long user1Id = service.postUser(userDto1).getId();
        Long user2Id = service.postUser(userDto2).getId();

        List<UserDto> users = service.getUsers(List.of(user1Id, user2Id), 0, 10);

        assertThat(users.getFirst().getName(), equalTo(userDto1.getName()));
        assertThat(users.getFirst().getEmail(), equalTo(userDto2.getEmail()));
    }

    @Test
    void testPostCategory() {
        NewCategoryDto dto = NewCategoryDto.builder()
                .name("famous")
                .build();

        CategoryDto category = service.postCategory(dto);

        assertThat(category.getName(), equalTo(dto.getName()));
    }

    @Test
    void testPostCompilation() {
        Category category = categoryRepository.save(Category.builder().name("asdadsadsasdasd").build());

        Event event1 = Event.builder()
                .eventDate(LocalDateTime.now())
                .title("dadadadd")
                .description("54rt3eggreteghtrghrtgshsertyh")
                .category(category)
                .build();

        Event event2 = Event.builder()
                .eventDate(LocalDateTime.now())
                .title("5435345345")
                .description("847345634532453543dgf432534535345dfg")
                .category(category)
                .build();

        Long event1Id = eventRepository.save(event1).getId();
        Long event2Id = eventRepository.save(event2).getId();

        RequestCompilationDto dto = RequestCompilationDto.builder()
                .events(List.of(event1Id, event2Id))
                .title("mainComp")
                .build();

        CompilationDto comp = service.postCompilation(dto);

        assertThat(comp.getTitle(), equalTo(dto.getTitle()));
        assertThat(comp.getEvents().getLast().getTitle(), equalTo(event2.getTitle()));
    }

    @Test
    void testGetEvents() {
        Category category = categoryRepository.save(Category.builder().name("asdadsadsasdasd").build());

        Event event1 = Event.builder()
                .eventDate(LocalDateTime.now())
                .title("dadadadd")
                .description("54rt3eggreteghtrghrtgshsertyh")
                .category(category)
                .build();

        Event event2 = Event.builder()
                .eventDate(LocalDateTime.now())
                .title("5435345345")
                .description("847345634532453543dgf432534535345dfg")
                .category(category)
                .build();

        Long event1Id = eventRepository.save(event1).getId();
        Long event2Id = eventRepository.save(event2).getId();

        SearchProperties prop = SearchProperties.builder()
                .from(0)
                .size(0)
                .build();

        List<EventFullDto> events = service.getEvents(prop);

        assertThat(events.getLast().getTitle(), equalTo(event2.getTitle()));   
    }
}
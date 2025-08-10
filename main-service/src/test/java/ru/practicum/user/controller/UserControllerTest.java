package ru.practicum.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Location;
import ru.practicum.user.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockBean
    UserService service;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void testSuccessValidPostEvent() throws Exception {
        NewEventDto dto  = NewEventDto.builder()
                .annotation("nullas;polfjkghslkdjfhgps;xioxdhgpisjdfhg[ipsjdhgiujsedhsdofighospxjdfghg[soajh")
                .category(3L)
                .description("nullaerfpw0[i otgjtpwojetghpoijshguojsdghjgoipsdghjogshjdojghjsdojhjgsdohgp[ojdshjnng")
                .eventDate("null")
                .location(Location.builder().build())
                .title("null")
                .build();

        mvc.perform(post("/users/3/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(201));
    }

    @Test
    void testSuccessValidPatchUserEvent() throws Exception {
        NewEventDto dto  = NewEventDto.builder().build();

        mvc.perform(patch("/users/3/events/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(200));
    }

    @Test
    void testSuccessValidPostComment() throws Exception {
        NewCommentDto dto = NewCommentDto.builder()
                .eventId(3L)
                .userId(3L)
                .text("null")
                .build();

        mvc.perform(post("/users/3/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(201));
    }

    @Test
    void testSuccessValidPatchComment() throws Exception {
        NewCommentDto dto = NewCommentDto.builder()
                .eventId(3L)
                .userId(3L)
                .text("null")
                .build();

        mvc.perform(patch("/users/3/comments/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(200));
    }
}
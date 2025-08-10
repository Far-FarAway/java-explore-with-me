package ru.practicum.user.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.compilation.dto.RequestCompilationDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.user.admin.service.AdminService;
import ru.practicum.user.dto.NewUserRequest;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AdminController.class)
class AdminControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    AdminService service;
    @Autowired
    ObjectMapper mapper;

    @Test
    void testSuccessValidPostUser() throws Exception {
        NewUserRequest dto = NewUserRequest.builder()
                .email("fsjlgakhnojhfsdg@gmail.com")
                .name("Kontai")
                .build();

        mvc.perform(post("/admin/users")
                .content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    void testSuccessValidPostCategory() throws Exception {
        NewCategoryDto dto = NewCategoryDto.builder()
                .name("Kontai")
                .build();

        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    void testSuccessValidPatch() throws Exception {
        NewCategoryDto dto = NewCategoryDto.builder()
                .name("null")
                .build();

        mvc.perform(patch("/admin/categories/3")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    void testSuccessValidPostCompilation() throws Exception {
        RequestCompilationDto dto = RequestCompilationDto.builder()
                .title("dkdkdkdkkdkd")
                .build();

        mvc.perform(post("/admin/compilations")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    void testSuccessValidPatchCompilation() throws Exception {
        RequestCompilationDto dto = RequestCompilationDto.builder().build();

        mvc.perform(patch("/admin/compilations/3")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    void testSuccessValidPatchEvent() throws Exception {
        NewEventDto dto = NewEventDto.builder().build();

        mvc.perform(patch("/admin/events/3")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    void testSuccessValidPatchComment() throws Exception {
        NewCommentDto dto = NewCommentDto.builder()
                .text("sdsdsdsdsd")
                .userId(3L)
                .eventId(3L)
                .build();

        mvc.perform(patch("/admin/comments/3")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }
}
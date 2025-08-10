package ru.practicum.user.admin.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import ru.practicum.Client;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.compilation.dto.RequestCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.admin.repository.AdminRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AdminServiceUnitTest {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Mock
    AdminRepository adminRepository;
    @Mock
    CategoryRepository catRepository;
    @Mock
    EventRepository eventRepository;
    @Mock
    CompilationRepository compRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    CategoryMapper catMapper;
    @Mock
    CompilationMapper compMapper;
    @Mock
    EventMapper eventMapper;
    @Mock
    CommentMapper commentMapper;
    @Mock
    Client client;
    @InjectMocks
    @Autowired
    AdminServiceImpl service;


    @Test
    void testDeleteUser() {
        Long userId = 343212L;

        Mockito.when(adminRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        service.deleteUser(userId);

        Mockito.verify(adminRepository).deleteById(userId);
    }

    @Test
    void testDeleteCategory() {
        Long catId = 123132L;

        Mockito.when(catRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        Mockito.when(eventRepository.existsByCategory_Id(Mockito.anyLong()))
                .thenReturn(false);

        service.deleteCategory(catId);

        Mockito.verify(catRepository).deleteById(catId);
    }

    @Test
    void testDeleteComment() {
        Long commId = 343212L;

        Mockito.when(commentRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        service.deleteComment(commId);

        Mockito.verify(commentRepository).deleteById(commId);
    }

    @Test
    void testPatchCategory() {
        Category cat = Category.builder()
                .id(33L)
                .name("TinkyWinky")
                .build();

        NewCategoryDto dto = NewCategoryDto.builder()
                .name("Watermelon")
                .build();

        Category finalCat = Category.builder()
                .id(cat.getId())
                .name(dto.getName())
                .build();

        Mockito.when(catRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(cat));

        service.patchCategory(dto, cat.getId());

        Mockito.verify(catRepository).save(finalCat);
    }

    @Test
    void testPatchCompilation() {
        Event event = Event.builder()
                .eventDate(LocalDateTime.now())
                .title("dadadadd")
                .description("54rt3eggreteghtrghrtgshsertyh")
                .category(Category.builder()
                        .name("TinkyWinky")
                        .build())
                .build();

        Compilation comp = Compilation.builder()
                .id(43L)
                .pinned(false)
                .title("Rumpel")
                .events(new ArrayList<>())
                .build();

        RequestCompilationDto dto = RequestCompilationDto.builder()
                .pinned(true)
                .title("fsfsfsfsfsfsf")
                .events(List.of(1L))
                .build();

        Compilation finalComp = Compilation.builder()
                .id(comp.getId())
                .pinned(dto.isPinned())
                .title(dto.getTitle())
                .events(List.of(event))
                .build();

        Mockito.when(compRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(comp));

        Mockito.when(eventRepository.findByIdIn(Mockito.anyList()))
                .thenReturn(List.of(event));

        service.patchCompilation(dto, comp.getId());

        Mockito.verify(compRepository).save(finalComp);
    }

    @Test
    void testPatchEvent() {
        Event event = Event.builder()
                .id(3343L)
                .eventDate(LocalDateTime.now())
                .title("dadadadd")
                .description("54rt3eggreteghtrghrtgshsertyh")
                .category(Category.builder()
                        .name("TinkyWinky")
                        .build())
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
                .build();

        Mockito.when(eventRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(event));

        Mockito.when(client.getStats(Mockito.anyMap()))
                        .thenReturn(ResponseEntity.of(Optional.of(List.of())));

        finalEvent.setCreatedOn(null);
        event.setCreatedOn(null);

        service.patchEvent(dto, event.getId());

        Mockito.verify(eventRepository).save(finalEvent);
    }
}
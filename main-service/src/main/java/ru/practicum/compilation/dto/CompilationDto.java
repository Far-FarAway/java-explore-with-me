package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.practicum.event.model.Event;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class CompilationDto {
    @Builder.Default
    private List<Event> events = new ArrayList<>();
    @Positive
    private Long id;
    @NotNull
    private boolean pinned;
    @NotNull
    @NotBlank
    private String title;
}

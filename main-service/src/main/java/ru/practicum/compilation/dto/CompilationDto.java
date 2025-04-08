package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.practicum.event.model.Event;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
public class CompilationDto {
    @Builder.Default
    private Set<Event> events = new HashSet<>();
    @Positive
    private Long id;
    @NotNull
    private boolean pinned;
    @NotNull
    @NotBlank
    private String title;
}

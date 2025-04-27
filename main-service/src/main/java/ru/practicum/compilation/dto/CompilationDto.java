package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    @Builder.Default
    List<EventShortDto> events = new ArrayList<>();
    @Positive
    Long id;
    @NotNull
    boolean pinned;
    @NotNull
    @NotBlank
    String title;
}

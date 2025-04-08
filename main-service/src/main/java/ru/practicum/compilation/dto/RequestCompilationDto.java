package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.marker.OnCreate;
import java.util.List;

@Builder
@Data
public class RequestCompilationDto {
    private List<Long> events;
    @NotNull(groups = OnCreate.class)
    private boolean pinned;
    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    private String title;
}

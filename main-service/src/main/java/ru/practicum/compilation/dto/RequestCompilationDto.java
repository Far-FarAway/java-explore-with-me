package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.marker.OnCreate;
import ru.practicum.marker.OnUpdate;

import java.util.List;

@Builder
@Data
public class RequestCompilationDto {
    private List<Long> events;
    @NotNull(groups = OnCreate.class)
    private boolean pinned;
    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    @Length(groups = {OnCreate.class, OnUpdate.class},
            max = 50)
    private String title;
}

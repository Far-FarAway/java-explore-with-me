package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.marker.OnCreate;
import ru.practicum.marker.OnUpdate;

import java.util.List;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCompilationDto {
    List<Long> events;
    @NotNull(groups = OnCreate.class)
    boolean pinned;
    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    @Length(groups = {OnCreate.class, OnUpdate.class},
            max = 50)
    String title;
}

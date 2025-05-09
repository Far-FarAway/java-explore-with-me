package ru.practicum.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.Location;
import ru.practicum.marker.OnCreate;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventRequest {
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    String annotation;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    CategoryDto category;
    String description;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    String eventDate;
    @NotNull(groups = OnCreate.class)
    Location location;
    @NotNull(groups = OnCreate.class)
    boolean paid;
    @PositiveOrZero
    @Builder.Default
    int participantLimit = 0;
    @Builder.Default
    boolean requestModeration = true;
    String state;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    String title;
}

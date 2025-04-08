package ru.practicum.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.marker.OnCreate;
import ru.practicum.user.dto.UserShortDto;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    String annotation;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    CategoryDto category;
    @PositiveOrZero
    int confirmRequests;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    String eventDate;
    Long id;
    @NotNull(groups = OnCreate.class)
    UserShortDto initiator;
    @NotNull(groups = OnCreate.class)
    boolean paid;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    String title;
    @PositiveOrZero
    @Builder.Default
    Long views = 0L;
}

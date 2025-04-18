package ru.practicum.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateAction;
import ru.practicum.marker.OnCreate;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    String annotation;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    Long category;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
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
    Integer participantLimit = 0;
    @Builder.Default
    Boolean requestModeration = true;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    String title;
    StateAction stateAction;
}

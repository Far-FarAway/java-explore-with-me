package ru.practicum.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateAction;
import ru.practicum.marker.OnCreate;
import ru.practicum.marker.OnUpdate;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    @Length(groups = {OnCreate.class, OnUpdate.class},
            min = 20, max = 2000)
    String annotation;
    @NotNull(groups = OnCreate.class)
    Long category;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    @Length(groups = {OnCreate.class, OnUpdate.class},
            min = 20, max = 7000)
    String description;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    String eventDate;
    @NotNull(groups = OnCreate.class)
    Location location;
    @Builder.Default
    Boolean paid = false;
    @PositiveOrZero(groups = {OnCreate.class, OnUpdate.class})
    @Builder.Default
    Integer participantLimit = 0;
    @Builder.Default
    Boolean requestModeration = true;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    @Length(groups = {OnCreate.class, OnUpdate.class},
            min = 3, max = 120)
    String title;
    StateAction stateAction;
}

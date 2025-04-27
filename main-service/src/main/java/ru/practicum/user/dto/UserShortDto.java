package ru.practicum.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.marker.OnCreate;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserShortDto {
    @Positive
    Long id;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    String name;
}

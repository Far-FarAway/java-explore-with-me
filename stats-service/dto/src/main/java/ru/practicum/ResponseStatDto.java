package ru.practicum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseStatDto {
    @NotBlank
    @NotNull
    String app;
    @NotBlank
    @NotNull
    String uri;
    @PositiveOrZero
    Long hits;
}

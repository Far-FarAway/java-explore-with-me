package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentDto {
    @NotNull
    @Positive
    Long eventId;
    @NotNull
    @Positive
    Long userId;
    @NotNull
    @NotBlank
    String text;
}

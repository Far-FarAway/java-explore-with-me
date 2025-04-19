package ru.practicum.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.practicum.marker.OnCreate;

@Builder
@Data
public class CategoryDto {
    @Positive
    private Long id;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    private String name;
}

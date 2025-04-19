package ru.practicum.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.marker.OnCreate;
import ru.practicum.marker.OnUpdate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    @Length(groups = {OnCreate.class, OnUpdate.class},
            max = 50)
    private String name;
}

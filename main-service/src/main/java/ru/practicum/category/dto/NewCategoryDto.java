package ru.practicum.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.marker.OnCreate;
import ru.practicum.marker.OnUpdate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCategoryDto {
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    @Length(groups = {OnCreate.class, OnUpdate.class},
            max = 50)
    String name;
}

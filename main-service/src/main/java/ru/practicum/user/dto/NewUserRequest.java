package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.marker.OnCreate;
import ru.practicum.marker.OnUpdate;

@Builder
@Data
public class NewUserRequest {
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    @Email(groups = OnCreate.class)
    @Length(groups = {OnCreate.class, OnUpdate.class},
            min = 6, max = 254)
    private String email;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    @Length(groups = {OnCreate.class, OnUpdate.class},
            min = 2, max = 250)
    private String name;
}

package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.marker.OnCreate;

@Builder
@Data
public class NewUserRequest {
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    @Email(groups = OnCreate.class)
    private String email;
    @NotBlank(groups = OnCreate.class)
    @NotNull(groups = OnCreate.class)
    private String name;
}

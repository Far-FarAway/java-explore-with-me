package ru.practicum.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exception.InternalServerException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@Component
public class UserMapper {
    public User toEntity(NewUserRequest dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public UserDto mapDto(User user) {
        if (user.getId() == null) {
            throw new InternalServerException("User if is null");
        }

        if (user.getName().isEmpty() || user.getName().isBlank()) {
            throw new InternalServerException("User name is null or blank");
        }

        if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new InternalServerException("User email is null or blank");
        }

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortDto mapShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}

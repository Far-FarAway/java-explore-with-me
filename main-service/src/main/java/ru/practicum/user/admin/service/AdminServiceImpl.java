package ru.practicum.user.admin.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.admin.repository.AdminRepository;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminServiceImpl implements AdminService {
    AdminRepository repository;
    CategoryRepository catRepository;
    EventRepository eventRepository;
    UserMapper mapper;
    CategoryMapper catMapper;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        return repository.findByIdIn(ids).stream()
                .map(mapper::mapDto)
                .skip(from)
                .limit(size)
                .toList();
    }

    @Override
    public UserDto postUser(NewUserRequest user) {
        return mapper.mapDto(repository.save(mapper.mapPOJO(user)));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        repository.deleteById(userId);
    }

    @Override
    public CategoryDto postCategory(NewCategoryDto dto) {
        return catMapper.mapDto(catRepository.save(catMapper.mapPOJO(dto)));
    }

    @Override
    public void deleteCategory(Long catId) {
        if (!catRepository.existsById(catId)) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }

        if (eventRepository.existsByCategory_Id(catId)) {
            throw new ConflictException("The category is not empty",
                    "For the requested operation the conditions are not met.");
        }

        catRepository.deleteById(catId);
    }

    @Override
    public CategoryDto patchCategory(NewCategoryDto dto, Long catId) {
        if (catRepository.existsById(catId)) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }

        Category cat = catMapper.mapPOJO(dto);

        cat.setId(catId);

        return catMapper.mapDto(catRepository.save(cat));
    }
}

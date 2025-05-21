package ru.practicum.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.InternalServerException;

@Component
public class CategoryMapper {
    public Category toEntity(NewCategoryDto dto) {
        if (dto.getName().isEmpty() || dto.getName().isBlank()) {
            throw new ConflictException("Category name is null or blank", "Conflict with class field");
        }

        return  Category.builder()
                .name(dto.getName())
                .build();
    }

    public CategoryDto mapDto(Category category) {
        if (category == null) {
            throw new InternalServerException("Category is null");
        }

        if (category.getName().isEmpty() || category.getName().isBlank()) {
            throw new InternalServerException("Category name is null or blank");
        }

        if (category.getId() == null) {
            throw new InternalServerException("Category id is null");
        }

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
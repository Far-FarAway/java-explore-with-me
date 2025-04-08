package ru.practicum.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

@Component
public class CategoryMapper {
    public Category mapPOJO(CategoryDto dto) {
        return  Category.builder()
                .name(dto.getName())
                .build();
    }

    public Category mapPOJO(NewCategoryDto dto) {
        return  Category.builder()
                .name(dto.getName())
                .build();
    }

    public CategoryDto mapDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}

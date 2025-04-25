package ru.practicum.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

@Component
public class CategoryMapper {
    public Category toEntity(CategoryDto dto) {
        return  Category.builder()
                .name(dto.getName())
                .build();
    }

    public Category toEntity(NewCategoryDto dto) {
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
//Не совсем понял, зачем делать проверку, если при сериализации json идет проверка тех полей, которые не должны быть
//пусты, а при вставке данных в БД уже проевряются ограничения столбцов и тоже будет выбрасываться исключение
//получается нужные поля не будут пустыми, а если будут, то исключение выброситься самостоятельно

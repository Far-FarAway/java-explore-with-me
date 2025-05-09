package ru.practicum.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository repository;
    CategoryMapper mapper;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).stream()
                .map(mapper::mapDto)
                .toList();
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        Category cat = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " was not found"));

        return mapper.mapDto(cat);
    }
}

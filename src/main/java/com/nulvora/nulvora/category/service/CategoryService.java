package com.nulvora.nulvora.category.service;

import com.nulvora.nulvora.category.dto.request.CategoryRequest;
import com.nulvora.nulvora.category.dto.response.CategoryResponse;
import com.nulvora.nulvora.category.entity.Category;
import com.nulvora.nulvora.category.mapper.CategoryMapper;
import com.nulvora.nulvora.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryResponse createCategory(CategoryRequest request){
        Category category = categoryRepository.save(categoryMapper.toEntity(request));

        return categoryMapper.toCategoryResponse(category);
    }

}

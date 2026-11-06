package com.nulvora.nulvora.category.service;

import com.nulvora.nulvora.category.dto.request.CategoryRequest;
import com.nulvora.nulvora.category.dto.response.CategoryResponse;
import com.nulvora.nulvora.category.entity.Category;
import com.nulvora.nulvora.category.mapper.CategoryMapper;
import com.nulvora.nulvora.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryResponse createCategory(CategoryRequest request){
        Category category = categoryRepository.save(categoryMapper.toEntity(request));
        return categoryMapper.toCategoryResponse(category);
    }


    // verificar se eixste categoria pelo id
    public Category getCategoryByIdOrThrow(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Categoria não encontrada")
                );
    }

}

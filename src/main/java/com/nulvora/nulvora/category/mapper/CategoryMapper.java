package com.nulvora.nulvora.category.mapper;

import com.nulvora.nulvora.category.dto.request.CategoryRequest;
import com.nulvora.nulvora.category.dto.response.CategoryResponse;
import com.nulvora.nulvora.category.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);
}

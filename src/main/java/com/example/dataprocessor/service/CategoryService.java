package com.example.dataprocessor.service;

import com.example.dataprocessor.entity.CategoryEntity;
import com.example.dataprocessor.exception.CategoryInUseException;
import com.example.dataprocessor.exception.CategoryNotFoundException;
import com.example.dataprocessor.mapper.CategoryMapper;
import com.example.dataprocessor.model.dto.CategoryRequest;
import com.example.dataprocessor.model.dto.CategoryResponse;
import com.example.dataprocessor.repository.CategoryRepository;
import com.example.dataprocessor.repository.DataRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final DataRepository dataRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper,
            DataRepository dataRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.dataRepository = dataRepository;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CategoryRequest request) {
        CategoryEntity entity = categoryMapper.toEntity(request);
        CategoryEntity saved = categoryRepository.save(entity);
        return categoryMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@securityCheck.isOwner(#id) or hasRole('ADMIN')")
    public CategoryResponse getCategoryById(Long id) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        return categoryMapper.toResponse(entity);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        entity.setName(request.name());

        return categoryMapper.toResponse(entity);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or (hasRole('MANAGER') and #id >= 10)")
    public void deleteCategory(Long id) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));


        if (dataRepository.existsByCategoryId(id)) {
            throw new CategoryInUseException(id);
        }

        categoryRepository.delete(entity);
    }
}

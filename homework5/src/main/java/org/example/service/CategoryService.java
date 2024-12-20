package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CategoryRequest;
import org.example.dto.response.CategoryResponse;
import org.example.model.Category;
import org.example.pattern.observer.impl.ObservableImpl;
import org.example.repository.CustomRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService extends ObservableImpl<Category> {

    private final CustomRepository<Category> repository;

    public CategoryService(CustomRepository<Category> repository) {
        this.repository = repository;
        addObserver(repository);
    }

    public Collection<CategoryRequest> getAllCategories() {
        log.info("Fetching all categories.");
        Collection<CategoryRequest> categories = repository
                .findAll()
                .stream()
                .map(this::parseModelToRequest)
                .collect(Collectors.toList());
        log.info("Fetched {} categories.", categories.size());
        return categories;
    }

    public CategoryRequest getCategoryById(Long id) {
        log.info("Fetching category by id: {}", id);
        CategoryRequest category = parseModelToRequest(repository.findById(id));
        log.info("Fetched category: {}", category);
        return category;
    }

    public Long createCategory(CategoryRequest request) {
        log.info("Creating category: {}", request);
        Long id = repository.save(parseRequestToModel(request));
        log.info("Category '{}' created successfully with id: {}", request.getName(), id);
        return id;
    }

    public void createCategory(CategoryResponse response) {
        log.info("Creating category from response: {}", response);
        notifyObservers(parseResponseToModel(response));
        log.info("Category '{}' created successfully from response.", response.getName());
    }

    public void updateCategory(Long id, CategoryRequest request) {
        log.info("Updating category with id: {} with data: {}", id, request);
        repository.update(id, parseRequestToModel(request));
        log.info("Category with id {} updated successfully.", id);
    }

    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);
        repository.delete(id);
        log.info("Category with id {} deleted successfully.", id);
    }

    private CategoryRequest parseModelToRequest(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryRequest(category.getSlug(), category.getName());
    }

    private Category parseRequestToModel(CategoryRequest request) {
        return new Category(request.getSlug(), request.getName());
    }

    private Category parseResponseToModel(CategoryResponse response) {
        return new Category(response.getSlug(), response.getName());
    }

    public void restore() {
        repository.restore();
    }

}
package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.CategoryRequest;
import org.example.service.CategoryService;
import org.example.timed.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/places/categories")
@RequiredArgsConstructor
@Timed
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryRequest> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryRequest> getCategoryById(@PathVariable Long id) {
        CategoryRequest category = categoryService.getCategoryById(id);

        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }

    @PostMapping
    public Long createCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @PostMapping("/restore")
    public void restore() {
        categoryService.restore();
    }

}
package org.example.service;

import org.example.dto.request.CategoryRequest;
import org.example.dto.response.CategoryResponse;
import org.example.model.Category;
import org.example.repository.CustomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CustomRepository<Category> repository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() {
        Category airportCategory = createCategory("airports", "Аэропорты");
        Category amusementCategory = createCategory("amusement", "Развлечения");
        when(repository.findAll()).thenReturn(Arrays.asList(airportCategory, amusementCategory));

        Collection<CategoryRequest> result = categoryService.getAllCategories();

        assertThat(result).hasSize(2);

        Iterator<CategoryRequest> iterator = result.iterator();
        assertThat(iterator.next().getName()).isEqualTo("Аэропорты");
        assertThat(iterator.next().getSlug()).isEqualTo("amusement");
    }

    @Test
    void testGetCategoryById() {
        Category airportCategory = createCategory("airports", "Аэропорты");
        when(repository.findById(1L)).thenReturn(airportCategory);

        CategoryRequest result = categoryService.getCategoryById(1L);

        assertThat(result.getName()).isEqualTo("Аэропорты");
    }

    @Test
    void testCreateCategory() {
        CategoryRequest request = createCategoryRequest("airports", "Аэропорты");
        when(repository.save(any(Category.class))).thenReturn(1L);

        Long result = categoryService.createCategory(request);

        assertThat(result).isEqualTo(1L);
    }

    @Test
    void testCreateLocationDuringInitialize() {
        CategoryResponse response = new CategoryResponse();
        response.setSlug("airports");
        response.setName("Аэропорты");

        categoryService.createCategory(response);

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(repository).save(categoryCaptor.capture());

        List<Category> capturedCategories = categoryCaptor.getAllValues();
        assertThat(capturedCategories).hasSize(1);

        Category capturedCategory = capturedCategories.getFirst();
        assertThat(capturedCategory.getSlug()).isEqualTo("airports");
        assertThat(capturedCategory.getName()).isEqualTo("Аэропорты");
    }

    @Test
    void testUpdateCategoryById() {
        CategoryRequest request = createCategoryRequest("airports", "Аэропорты");

        categoryService.updateCategory(1L, request);

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(repository).update(eq(1L), categoryCaptor.capture());

        List<Category> capturedCategories = categoryCaptor.getAllValues();
        assertThat(capturedCategories).hasSize(1);

        Category capturedCategory = capturedCategories.getFirst();
        assertThat(capturedCategory.getName()).isEqualTo("Аэропорты");
        assertThat(capturedCategory.getSlug()).isEqualTo("airports");
    }

    @Test
    void testDeleteCategoryById() {
        categoryService.deleteCategory(1L);

        verify(repository).delete(1L);
    }

    @Test
    void testGetCategoryByNonExistentId() {
        when(repository.findById(1L)).thenReturn(null);

        CategoryRequest result = categoryService.getCategoryById(1L);

        assertThat(result).isNull();
    }

    @Test
    void testUpdateCategoryByNonExistentId() {
        CategoryRequest request = createCategoryRequest("airports", "Аэропорты");
        doThrow(new IllegalArgumentException()).when(repository).update(eq(1L), any(Category.class));

        assertThatThrownBy(() -> categoryService.updateCategory(1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testDeleteCategoryByNonExistentId() {
        doThrow(new IllegalArgumentException()).when(repository).delete(1L);

        assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Category createCategory(String slug, String name) {
        return new Category(slug, name);
    }

    private CategoryRequest createCategoryRequest(String slug, String name) {
        return new CategoryRequest(slug, name);
    }

}
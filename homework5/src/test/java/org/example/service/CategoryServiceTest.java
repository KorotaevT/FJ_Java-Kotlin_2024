package org.example.service;

import org.example.dto.request.CategoryRequest;
import org.example.dto.response.CategoryResponse;
import org.example.model.Category;
import org.example.pattern.observer.Observer;
import org.example.pattern.observer.impl.ObservableImpl;
import org.example.repository.CustomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.MockObjects.AIRPORTS_NAME;
import static org.example.MockObjects.AIRPORTS_SLUG;
import static org.example.MockObjects.AMUSEMENT_SLUG;
import static org.example.MockObjects.createCategoryRequest;
import static org.example.MockObjects.getCategoriesAsCategory;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CustomRepository<Category> repository;

    @Mock
    private Observer<Category> observer;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() {
        when(repository.findAll()).thenReturn(getCategoriesAsCategory());

        Collection<CategoryRequest> result = categoryService.getAllCategories();

        assertThat(result).hasSize(2);

        Iterator<CategoryRequest> iterator = result.iterator();
        assertThat(iterator.next().getName()).isEqualTo(AIRPORTS_NAME);
        assertThat(iterator.next().getSlug()).isEqualTo(AMUSEMENT_SLUG);
    }

    @Test
    void testGetCategoryById() {
        when(repository.findById(1L)).thenReturn(getCategoriesAsCategory().getFirst());

        CategoryRequest result = categoryService.getCategoryById(1L);

        assertThat(result.getName()).isEqualTo(AIRPORTS_NAME);
    }

    @Test
    void testCreateCategory() {
        CategoryRequest request = createCategoryRequest(AIRPORTS_SLUG, AIRPORTS_NAME);
        when(repository.save(any(Category.class))).thenReturn(1L);

        Long result = categoryService.createCategory(request);

        assertThat(result).isEqualTo(1L);
    }

    @Test
    void testCreateLocationDuringInitialize() {
        ObservableImpl<Category> observable = new ObservableImpl<>();
        observable.addObserver(observer);

        CategoryResponse response = new CategoryResponse();
        response.setSlug(AIRPORTS_SLUG);
        response.setName(AIRPORTS_NAME);

        Category category = new Category(response.getSlug(), response.getName());
        observable.notifyObservers(category);

        verify(observer, times(1)).update(any(Category.class));
    }


    @Test
    void testUpdateCategoryById() {
        CategoryRequest request = createCategoryRequest(AIRPORTS_SLUG, AIRPORTS_NAME);

        categoryService.updateCategory(1L, request);

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(repository).update(eq(1L), categoryCaptor.capture());

        Category capturedCategory = categoryCaptor.getValue();
        assertThat(capturedCategory.getName()).isEqualTo(AIRPORTS_NAME);
        assertThat(capturedCategory.getSlug()).isEqualTo(AIRPORTS_SLUG);
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
        CategoryRequest request = createCategoryRequest(AIRPORTS_SLUG, AIRPORTS_NAME);
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

}
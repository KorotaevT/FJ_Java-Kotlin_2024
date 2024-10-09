package org.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.example.dto.request.CategoryRequest;
import org.example.service.CategoryService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({CategoryController.class})
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllCategories() throws Exception {
        List<CategoryRequest> categories = Arrays.asList(
                createCategoryRequest("airports", "Аэропорты"),
                createCategoryRequest("amusement", "Развлечения")
        );

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                {
                                    "slug": "airports",
                                    "name": "Аэропорты"
                                },
                                {
                                    "slug": "amusement",
                                    "name": "Развлечения"
                                }
                                ]
                                """
                ));
    }

    @Test
    public void getCategoryById() throws Exception {
        CategoryRequest request = createCategoryRequest("airports", "Аэропорты");

        when(categoryService.getCategoryById(anyLong())).thenReturn(request);

        mockMvc.perform(get("/api/v1/places/categories/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                    "slug": "airports",
                                    "name": "Аэропорты"
                                }
                                """
                ));
    }

    @Test
    public void getCategoryByNonExistentId() throws Exception {
        when(categoryService.getCategoryById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/v1/places/categories/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createCategory() throws Exception {
        String categoryRequest = """
                {
                    "slug": "airports",
                    "name": "Аэропорты"
                }
                """;

        when(categoryService.createCategory(any(CategoryRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/api/v1/places/categories")
                        .content(categoryRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void createCategory_InvalidRequest() throws Exception {
        String invalidCategoryRequest = """
                {
                    "slug": "",
                    "name": ""
                }
                """;

        mockMvc.perform(post("/api/v1/places/categories")
                        .content(invalidCategoryRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCategoryById() throws Exception {
        String categoryRequest = """
                {
                    "slug": "airports",
                    "name": "Аэропорты"
                }
                """;

        doNothing().when(categoryService).updateCategory(anyLong(), any(CategoryRequest.class));

        mockMvc.perform(put("/api/v1/places/categories/{id}", 1)
                        .content(categoryRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCategoryById() throws Exception {
        doNothing().when(categoryService).deleteCategory(anyLong());

        mockMvc.perform(delete("/api/v1/places/categories/{id}", 1))
                .andExpect(status().isOk());
    }

    private CategoryRequest createCategoryRequest(String slug, String name) {
        return new CategoryRequest(slug, name);
    }

}
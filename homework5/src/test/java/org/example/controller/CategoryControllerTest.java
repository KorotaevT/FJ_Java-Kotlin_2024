package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.example.dto.request.CategoryRequest;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.jdbc.Sql;

import static org.example.MockObjects.userLoginRequest;

@RequiredArgsConstructor
public class CategoryControllerTest extends AbstractTestContainer {

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllCategories() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);

        var categories = Arrays.asList(
                createCategoryRequest("airports", "Аэропорты"),
                createCategoryRequest("amusement", "Развлечения")
        );

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/places/categories")
                .header("Authorization", token))
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
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);

        var request = createCategoryRequest("airports", "Аэропорты");

        when(categoryService.getCategoryById(anyLong())).thenReturn(request);

        mockMvc.perform(get("/api/v1/places/categories/{id}", 1)
                .header("Authorization", token))
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
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryByNonExistentId() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);

        when(categoryService.getCategoryById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/v1/places/categories/{id}", 999)
                .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCategory() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);

        String categoryRequest = """
                {
                    "slug": "airports",
                    "name": "Аэропорты"
                }
                """;

        when(categoryService.createCategory(any(CategoryRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/api/v1/places/categories")
                        .content(categoryRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCategory_InvalidRequest() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);

        String invalidCategoryRequest = """
                {
                    "slug": "",
                    "name": ""
                }
                """;

        mockMvc.perform(post("/api/v1/places/categories")
                        .content(invalidCategoryRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategoryById() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);

        String categoryRequest = """
                {
                    "slug": "airports",
                    "name": "Аэропорты"
                }
                """;

        doNothing().when(categoryService).updateCategory(anyLong(), any(CategoryRequest.class));

        mockMvc.perform(put("/api/v1/places/categories/{id}", 1)
                        .content(categoryRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCategoryById() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);

        doNothing().when(categoryService).deleteCategory(anyLong());

        mockMvc.perform(delete("/api/v1/places/categories/{id}", 1)
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    private CategoryRequest createCategoryRequest(String slug, String name) {
        return new CategoryRequest(slug, name);
    }

}
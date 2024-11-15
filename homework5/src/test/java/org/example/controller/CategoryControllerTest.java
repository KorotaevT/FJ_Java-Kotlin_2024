package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.example.dto.request.CategoryRequest;

import static org.example.MockObjects.CATEGORY_JSON;
import static org.example.MockObjects.CATEGORY_JSON_SINGLE;
import static org.example.MockObjects.INVALID_CATEGORY_JSON;
import static org.example.MockObjects.createCategoryRequest;
import static org.example.MockObjects.getCategories;
import static org.example.MockObjects.userLoginRequest;
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

@RequiredArgsConstructor
public class CategoryControllerTest extends AbstractTestContainer {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String API_CATEGORIES = "/api/v1/places/categories";
    private static final String API_CATEGORY_BY_ID = "/api/v1/places/categories/{id}";

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllCategories() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        var categories = getCategories();

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get(API_CATEGORIES)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().json(CATEGORY_JSON));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        var request = createCategoryRequest("airports", "Аэропорты");

        when(categoryService.getCategoryById(anyLong())).thenReturn(request);

        mockMvc.perform(get(API_CATEGORY_BY_ID, 1)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().json(CATEGORY_JSON_SINGLE));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryByNonExistentId() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        when(categoryService.getCategoryById(anyLong())).thenReturn(null);

        mockMvc.perform(get(API_CATEGORY_BY_ID, 999)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCategory() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        when(categoryService.createCategory(any(CategoryRequest.class))).thenReturn(1L);

        mockMvc.perform(post(API_CATEGORIES)
                        .content(CATEGORY_JSON_SINGLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCategory_InvalidRequest() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        mockMvc.perform(post(API_CATEGORIES)
                        .content(INVALID_CATEGORY_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategoryById() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        doNothing().when(categoryService).updateCategory(anyLong(), any(CategoryRequest.class));

        mockMvc.perform(put(API_CATEGORY_BY_ID, 1)
                        .content(CATEGORY_JSON_SINGLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCategoryById() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        doNothing().when(categoryService).deleteCategory(anyLong());

        mockMvc.perform(delete(API_CATEGORY_BY_ID, 1)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());
    }

}
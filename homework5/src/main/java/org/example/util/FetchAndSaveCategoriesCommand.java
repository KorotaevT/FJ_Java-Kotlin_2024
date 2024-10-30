package org.example.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.CategoryResponse;
import org.example.pattern.command.Command;
import org.example.service.CategoryService;
import org.example.service.KudagoService;

@Slf4j
@RequiredArgsConstructor
public class FetchAndSaveCategoriesCommand implements Command {

    private final KudagoService kudagoService;
    private final CategoryService categoryService;

    @Override
    public void execute() {
        var categories = kudagoService.getCategories();

        if (categories == null) {
            log.info("No categories fetched. Received null response.");
        } else {
            log.info("Fetched {} categories.", categories.length);

            for (CategoryResponse category : categories) {
                categoryService.createCategory(category);
            }
        }
    }

}
package org.example.config;

import org.example.model.Category;
import org.example.model.Location;
import org.example.repository.CustomRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public CustomRepository<Category> categoryRepository() {
        return new CustomRepository<>();
    }

    @Bean
    public CustomRepository<Location> locationRepository() {
        return new CustomRepository<>();
    }

}
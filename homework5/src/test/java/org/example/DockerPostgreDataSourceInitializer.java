package org.example;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DockerPostgreDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
        postgreSQLContainer.start();

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                configurableApplicationContext,
                "main.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "main.datasource.username=" + postgreSQLContainer.getUsername(),
                "main.datasource.password=" + postgreSQLContainer.getPassword()
        );
    }

}
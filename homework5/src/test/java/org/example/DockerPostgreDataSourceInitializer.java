package org.example;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DockerPostgreDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final TestPostgreSQLContainer postgreSQLContainer = new TestPostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("db/init.sql")
            .withUrlParam("currentSchema", "public");

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

    static class TestPostgreSQLContainer extends PostgreSQLContainer<TestPostgreSQLContainer> {
        public TestPostgreSQLContainer(String dockerImageName) {
            super(dockerImageName);
        }
    }

}
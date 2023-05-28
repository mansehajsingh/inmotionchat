package com.inmotionchat.test;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class IntegrationTest {

    private static final String postgresDockerImage = "postgres:15.2";

    private static final String databaseName = "inmotion";

    private static final String databaseTestPassword = "testpassword";

    private static final String databaseTestUsername = "inmotionuser";

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(postgresDockerImage)
            .withDatabaseName(databaseName)
            .withUsername(databaseTestUsername)
            .withUsername(databaseTestPassword);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("JWT_SECRET", () -> "dfcce544-fb4e-4b90-9fe7-dccc63bd67fb");
    }

}

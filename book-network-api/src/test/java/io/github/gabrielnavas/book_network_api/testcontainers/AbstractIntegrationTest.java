package io.github.gabrielnavas.book_network_api.testcontainers;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
abstract public class AbstractIntegrationTest {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.4");

        private static void startContainers() {
            Startables.deepStart(Stream.of(postgres)).join();
        }

        private static Map<String, Object> connectionConfigsFromContainer() {
            return Map.of(
                    "spring.datasource.url", postgres.getJdbcUrl(),
                    "spring.datasource.username", postgres.getUsername(),
                    "spring.datasource.password", postgres.getPassword()
            );
        }

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            startContainers();
            Map<String, Object> connectionConfigs = connectionConfigsFromContainer();
            ensureContainerConfigsIsFirst(connectionConfigs, applicationContext);
        }

        private void ensureContainerConfigsIsFirst(
                Map<String, Object> connectionConfigs,
                ConfigurableApplicationContext applicationContext
        ) {
            MapPropertySource testcontainers =
                    new MapPropertySource("testcontainers", connectionConfigs);
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getPropertySources().addFirst(testcontainers);
        }


    }
}

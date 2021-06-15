package com.example.weatherapp;

import com.example.weatherapp.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
public class WeatherAppIntegrationTests {

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @Container
    public static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));

    @Autowired
    private MessageRepository repository;

    @Autowired
    private StreamBridge streamBridge;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrapServers", () -> kafka.getBootstrapServers());

        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void return_same_input_when_sending_payload() {
        // Arrange

        // Act
        streamBridge.send("identity-in-0", "hello");

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> !repository.findAllByMessage("hello").isEmpty());
    }

    @Test
    public void return_reverse_input_when_sending_payload() {
        // Arrange

        // Act
        streamBridge.send("reverse-in-0", "hello");

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> !repository.findAllByMessage("olleh").isEmpty());
    }

    @Test
    public void return_uppercase_and_reversed_input_when_sending_payload() {
        // Arrange

        // Act
        streamBridge.send("upperCaseReverseInput", "hello");

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> !repository.findAllByMessage("OLLEH").isEmpty());
    }
}

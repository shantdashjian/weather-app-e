package com.example.weatherapp;

import com.example.weatherapp.kafka.KafkaProducer;
import com.example.weatherapp.repository.MessageRepository;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
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
    private KafkaProducer producer;

    @Autowired
    private MessageRepository repository;

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
        producer.send("identity-in-0", "hello");

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> !repository.findAllByMessage("hello").isEmpty());
    }

    @Test
    public void return_reverse_input_when_sending_payload() {
        // Arrange

        // Act
        producer.send("reverse-in-0", "hello");

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> !repository.findAllByMessage("olleh").isEmpty());
    }

    @Test
    public void return_uppercase_and_reversed_input_when_sending_payload() {
        // Arrange

        // Act
        producer.send("upperCaseReverseInput", "hello");

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> !repository.findAllByMessage("OLLEH").isEmpty());
    }

    @TestConfiguration
    static class KafkaTestContainersConfiguration {

        @Bean
        public ProducerFactory<String, String> producerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        //
        @Bean
        public KafkaTemplate<String, String> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }
    }

}

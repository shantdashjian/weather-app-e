package com.example.weatherapp;

import com.example.weatherapp.repository.MessageRepository;
import com.example.weatherapp.schema.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.MimeType;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
public class WeatherAppIntegrationTests {

    public static final String APPLICATION_AVRO = "application/*+avro";

    private static Network network = Network.newNetwork();

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"))
            .withNetwork(network)
            .withNetworkAliases("kafkacontainer");

    @Container
    public static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));

    @Container
    public static GenericContainer schemaContainer = new GenericContainer(DockerImageName.parse("confluentinc/cp-schema-registry:latest"))
            .dependsOn(kafka)
            .withExposedPorts(8081)
            .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
            .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "kafkacontainer:9092")
            .withNetwork(network);

    @Autowired
    private MessageRepository repository;

    @Autowired
    private StreamBridge streamBridge;

    private MessageDto messageDto;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrapServers", () -> kafka.getBootstrapServers());

        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.cloud.stream.kafka.binder.producer-properties.schema.registry.url",
                () -> "http://" + schemaContainer.getHost() + ":" + schemaContainer.getFirstMappedPort());
    }

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        messageDto = MessageDto.newBuilder().setMessage("hello").build();
    }

    @Test
    public void return_same_input_when_sending_payload() {
        streamBridge.send("identity-in-0", messageDto, MimeType.valueOf(APPLICATION_AVRO));

        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> !repository.findAllByMessage("hello").isEmpty());
    }

    @Test
    public void return_reverse_input_when_sending_payload() {
        streamBridge.send("reverse-in-0", messageDto, MimeType.valueOf(APPLICATION_AVRO));

        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> !repository.findAllByMessage("olleh").isEmpty());
    }

    @Test
    public void return_uppercase_and_reversed_input_when_sending_payload() {
        streamBridge.send("upperCaseReverseInput", messageDto, MimeType.valueOf(APPLICATION_AVRO));

        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> !repository.findAllByMessage("OLLEH").isEmpty());
    }
}

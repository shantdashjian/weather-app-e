package com.example.weatherapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.FileWriter;
import java.io.IOException;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@Testcontainers
@SpringBootTest
public class WeatherAppIntegrationTests {

    public static final DockerImageName RABBITMQ_IMAGE = DockerImageName.parse("rabbitmq:3-management");

    @Container
    public static RabbitMQContainer rabbitmq = new RabbitMQContainer(RABBITMQ_IMAGE);

    @MockBean
    @Qualifier("identityFileWriter")
    private FileWriter identityFileWriter;

    @MockBean
    @Qualifier("reversedFileWriter")
    private FileWriter reversedFileWriter;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        String host = rabbitmq.getHost();
        Integer port = rabbitmq.getFirstMappedPort();
        registry.add("spring.rabbitmq.host", () -> host);
        registry.add("spring.rabbitmq.port", () -> port);
    }

    @Test
    public void return_same_input_when_sending_payload() throws IOException, InterruptedException {
        // Arrange

        // Act
        rabbitmq.execInContainer("rabbitmqadmin",
                "publish", "exchange=identity-in-0", "routing_key=\"\"", "payload=hello");

        // Assert
        verify(identityFileWriter, timeout(10000).times(1)).append("hello");
    }

    @Test
    public void return_reverse_input_when_sending_payload() throws IOException, InterruptedException {
        // Arrange

        // Act
        rabbitmq.execInContainer("rabbitmqadmin",
                "publish", "exchange=reverse-in-0", "routing_key=\"\"", "payload=hello");

        // Assert
        verify(reversedFileWriter).append("olleh");
    }

    @Test
    public void return_uppercase_and_reversed_input_when_sending_payload() throws IOException, InterruptedException {
        // Arrange

        // Act
        rabbitmq.execInContainer("rabbitmqadmin",
                "publish", "exchange=upperCaseReverseInput", "routing_key=\"\"", "payload=hello");

        // Assert
        verify(reversedFileWriter).append("OLLEH");
    }

}

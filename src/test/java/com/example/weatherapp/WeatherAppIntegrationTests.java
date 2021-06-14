package com.example.weatherapp;

import com.example.weatherapp.kafka.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@Testcontainers
@SpringBootTest
public class WeatherAppIntegrationTests {

    @MockBean
    @Qualifier("identityFileWriter")
    private FileWriter identityFileWriter;

    @MockBean
    @Qualifier("reversedFileWriter")
    private FileWriter reversedFileWriter;

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @Autowired
    private KafkaProducer producer;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        String bootstrapServers = kafka.getBootstrapServers();
        registry.add("spring.kafka.bootstrapServers", () -> bootstrapServers);
    }

    @Test
    public void return_same_input_when_sending_payload() throws IOException, InterruptedException, ExecutionException {
        // Arrange

        // Act
        producer.send("identity-in-0", "hello");

        // Assert
        verify(identityFileWriter, timeout(10000).times(1)).append("hello");
    }

    @Test
    public void return_reverse_input_when_sending_payload() throws IOException {
        // Arrange

        // Act
        producer.send("reverse-in-0", "hello");

        // Assert
        // Check that the file has the payload written to it
        verify(reversedFileWriter, timeout(10000).times(1)).append("olleh");
    }

    @Test
    public void return_uppercase_and_reversed_input_when_sending_payload() throws IOException {
        // Arrange

        // Act
        producer.send("upperCaseReverseInput", "hello");

        // Assert
        verify(reversedFileWriter, timeout(10000).times(1)).append("OLLEH");
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

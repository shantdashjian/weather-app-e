//package com.example.weatherapp;
//
//import com.example.weatherapp.kafka.KafkaProducer;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.*;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//
//import static org.mockito.Mockito.timeout;
//import static org.mockito.Mockito.verify;
//
//@Testcontainers
//@SpringBootTest
//public class WeatherAppIntegrationTests {
//
//    @MockBean
//    @Qualifier("identityFileWriter")
//    private FileWriter identityFileWriter;
//
//    @MockBean
//    @Qualifier("reversedFileWriter")
//    private FileWriter reversedFileWriter;
//
//    @Container
//    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
//
//    @Autowired
//    private KafkaProducer producer;
//
//    @DynamicPropertySource
//    static void registerPgProperties(DynamicPropertyRegistry registry) {
//        String host = kafka.getHost();
//        Integer port = kafka.getFirstMappedPort();
//        String bootstrapServers = kafka.getBootstrapServers();
//        System.out.println("In Dynamic: " + host + ":" + port);
//        registry.add("spring.kafka.host", () -> host);
//        registry.add("spring.kafka.port", () -> port);
//        registry.add("spring.kafka.bootstrapServers", () -> bootstrapServers);
//    }
//
//    static String bootstrapServers;
//
//    @BeforeEach
//    public void setup() {
//        bootstrapServers = kafka.getBootstrapServers();
//        System.out.println(kafka.getBootstrapServers());
//    }
//
//    @Test
//    public void return_same_input_when_sending_payload() throws IOException, InterruptedException, ExecutionException {
//        // Arrange
//
//        // Act
//        producer.send("identity-in-0", "hello");
////        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
////        org.testcontainers.containers.Container.ExecResult result =
////                kafka.execInContainer("/bin/sh",
////                        "-c" ,
////                        "kafka-topics --list --bootstrap-server " + bootstrapServers);
////        // bin/kafka-topics.sh --list --bootstrap-server
////        String kafkaCommand = "echo hello | kafka-console-producer --topic identity-in-0 --bootstrap-server " + bootstrapServers;
////        System.out.println("Running Kafka command: " + kafkaCommand);
////        org.testcontainers.containers.Container.ExecResult result =
////                kafka.execInContainer("/bin/sh", "-c",
////                        kafkaCommand);
////        System.out.println(result.getStdout());
//        // Assert
//        // Check that the file has the payload written to it
//
//            verify(identityFileWriter, timeout(10000).times(1)).append("hello");
//    }
//
//    @TestConfiguration
//    static class KafkaTestContainersConfiguration {
//
////        @Bean
////        ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() {
////            ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
////            factory.setConsumerFactory(consumerFactory());
////            return factory;
////        }
////
////        @Bean
////        public ConsumerFactory<Integer, String> consumerFactory() {
////            return new DefaultKafkaConsumerFactory<>(consumerConfigs());
////        }
////
////        @Bean
////        public Map<String, Object> consumerConfigs() {
////            Map<String, Object> props = new HashMap<>();
////            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
////            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
////            props.put(ConsumerConfig.GROUP_ID_CONFIG, "baeldung");
////            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
////            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
////            return props;
////        }
////
//        @Bean
//        public ProducerFactory<String, String> producerFactory() {
//            Map<String, Object> configProps = new HashMap<>();
//            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
//            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//            return new DefaultKafkaProducerFactory<>(configProps);
//        }
////
//        @Bean
//        public KafkaTemplate<String, String> kafkaTemplate() {
//            return new KafkaTemplate<>(producerFactory());
//        }
////
//    }
//
//}

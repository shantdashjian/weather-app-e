package com.example.weatherapp.learning;

import com.example.weatherapp.config.FunctionConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class TestMultipleFunctions {

    @Test
    public void testMultipleFunctions() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        FunctionConfiguration.class))
                .run("--spring.cloud.function.definition=uppercase;reverse")) {

            InputDestination inputDestination = context.getBean(InputDestination.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> inputMessage = MessageBuilder.withPayload("Hello".getBytes()).build();
            inputDestination.send(inputMessage, "uppercase-in-0");
            inputDestination.send(inputMessage, "reverse-in-0");

            Message<byte[]> outputMessage = outputDestination.receive(0, "uppercase-out-0");
            assertThat(outputMessage.getPayload()).isEqualTo("HELLO".getBytes());

            outputMessage = outputDestination.receive(0, "reverse-out-1");
            assertThat(outputMessage.getPayload()).isEqualTo("olleH".getBytes());
        }
    }
}

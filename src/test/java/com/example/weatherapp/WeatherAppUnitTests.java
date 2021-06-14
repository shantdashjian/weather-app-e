package com.example.weatherapp;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;

import java.io.FileWriter;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class WeatherAppUnitTests {

    @Autowired
    private InputDestination input;

    @Autowired
    private OutputDestination output;

    @MockBean
    @Qualifier("identityFileWriter")
    private FileWriter identityFileWriter;

    @MockBean
    @Qualifier("reversedFileWriter")
    private FileWriter reversedFileWriter;

    @Test
    public void return_same_input_when_sending_payload() throws IOException {
        // Arrange

        // Act
        this.input.send(new GenericMessage<byte[]>("hello".getBytes()),"identity-in-0");

        // Assert
        // Check that the file has the payload written to it
        verify(identityFileWriter).append("hello");
    }

    @RepeatedTest(3)
    public void return_each_input_when_sending_payload(RepetitionInfo repetitionInfo) throws IOException {
        // Arrange
        String payload = "hello" + repetitionInfo.getCurrentRepetition();
        // Act
        this.input.send(new GenericMessage<byte[]>(payload.getBytes()),"identity-in-0");

        // Assert
        // Check that the file has the payload written to it
        verify(identityFileWriter).append(payload);
    }

    @Test
    public void return_reverse_input_when_sending_payload() throws IOException {
        // Arrange

        // Act
        this.input.send(new GenericMessage<byte[]>("Hello".getBytes()), "reverse-in-0");

        // Assert
        // Check that the file has the payload written to it
        verify(reversedFileWriter).append("olleH");
    }

    @Test
    public void return_uppercase_input_when_sending_payload() {
        // Arrange

        // Act
        this.input.send(new GenericMessage<byte[]>("hello".getBytes()), "uppercase-in-0");

        // Assert
        assertThat(output.receive().getPayload()).isEqualTo("HELLO".getBytes());
    }

    @Test
    public void return_uppercase_and_reversed_input_when_sending_payload() throws IOException {
        // Arrange

        // Act
        this.input.send(new GenericMessage<byte[]>("hello".getBytes()), "upperCaseReverseInput");

        // Assert
        verify(reversedFileWriter).append("OLLEH");
    }
}

package com.example.weatherapp;

import com.example.weatherapp.config.FunctionConfiguration;
import com.example.weatherapp.entity.MessageEntity;
import com.example.weatherapp.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WeatherAppUnitTests {

    @Mock
    private MessageRepository repository;

    FunctionConfiguration configuration;

    final ArgumentCaptor<MessageEntity> messageCaptor = ArgumentCaptor.forClass(MessageEntity.class);

    @BeforeEach
    public void setup() {
        configuration = new FunctionConfiguration(repository);
    }

    @Test
    public void return_same_input_when_sending_payload() {
        // Arrange

        // Act
        configuration.identity().accept("hello");

        // Assert
        verify(repository).saveAndFlush(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getMessage()).isEqualTo("hello");
    }

    @RepeatedTest(3)
    public void return_each_input_when_sending_payload(RepetitionInfo repetitionInfo) {
        // Arrange
        String payload = "hello" + repetitionInfo.getCurrentRepetition();

        // Act
        configuration.identity().accept(payload);

        // Assert
        verify(repository).saveAndFlush(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getMessage()).isEqualTo(payload);
    }

    @Test
    public void return_reverse_input_when_sending_payload() {
        // Arrange
        String payload = "Hello";

        // Act
        configuration.reverse().accept(payload);

        // Assert
        verify(repository).saveAndFlush(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getMessage()).isEqualTo("olleH");
    }

    @Test
    public void return_uppercase_input_when_sending_payload() {
        // Arrange
        String payload = "Hello";

        // Act
        String uppercased = configuration.uppercase().apply(payload);

        // Assert
        assertThat(uppercased).isEqualTo("HELLO");
    }

    @Test
    public void return_uppercase_and_reversed_input_when_sending_payload() {
        // Arrange
        String payload = "Hello";

        // Act
        configuration.reverse().accept(configuration.uppercase().apply(payload));

        // Assert
        verify(repository).saveAndFlush(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getMessage()).isEqualTo("OLLEH");
    }
}

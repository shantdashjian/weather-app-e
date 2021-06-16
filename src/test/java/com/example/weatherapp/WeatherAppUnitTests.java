package com.example.weatherapp;

import com.example.weatherapp.config.FunctionConfig;
import com.example.weatherapp.entity.MessageEntity;
import com.example.weatherapp.repository.MessageRepository;
import com.example.weatherapp.schema.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WeatherAppUnitTests {

    private final MessageRepository repository = mock(MessageRepository.class);

    private final FunctionConfig configuration = new FunctionConfig(repository);

    private final ArgumentCaptor<MessageEntity> messageCaptor = ArgumentCaptor.forClass(MessageEntity.class);

    private MessageDto messageDto;

    @BeforeEach
    public void setup() {
        messageDto = getMessageDto("Hello");
    }

    @Test
    public void return_same_input_when_sending_payload() {
        configuration.identity().accept(messageDto);

        verify(repository).saveAndFlush(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getMessage()).isEqualTo("Hello");
    }

    @RepeatedTest(3)
    public void return_each_input_when_sending_payload(RepetitionInfo repetitionInfo) {
        String payload = "Hello" + repetitionInfo.getCurrentRepetition();
        MessageDto dto = getMessageDto(payload);
        configuration.identity().accept(dto);

        verify(repository).saveAndFlush(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getMessage()).isEqualTo(payload);
    }

    @Test
    public void return_reverse_input_when_sending_payload() {
        configuration.reverse().accept(messageDto);

        verify(repository).saveAndFlush(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getMessage()).isEqualTo("olleH");
    }

    @Test
    public void return_uppercase_input_when_sending_payload() {
        MessageDto uppercasedDto = configuration.uppercase().apply(messageDto);

        assertThat(uppercasedDto.getMessage()).isEqualTo("HELLO");
    }

    @Test
    public void return_uppercase_and_reversed_input_when_sending_payload() {
        configuration.reverse().accept(configuration.uppercase().apply(messageDto));

        verify(repository).saveAndFlush(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getMessage()).isEqualTo("OLLEH");
    }

    private MessageDto getMessageDto(String payload) {
        return MessageDto.newBuilder()
                .setMessage(payload)
                .build();
    }
}

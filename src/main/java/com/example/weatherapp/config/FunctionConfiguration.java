package com.example.weatherapp.config;

import com.example.weatherapp.entity.MessageEntity;
import com.example.weatherapp.repository.MessageRepository;
import com.example.weatherapp.schema.MessageDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class FunctionConfiguration {

    private final MessageRepository repository;

    public FunctionConfiguration(MessageRepository repository) {
        this.repository = repository;
    }

    @Bean
    public Consumer<MessageDto> identity() {
        return v -> saveMessage(v);
    }

    @Bean
    public Function<MessageDto, MessageDto> uppercase() {
        return v ->
                MessageDto.newBuilder()
                        .setMessage(v.getMessage().toUpperCase())
                        .build();
    }

    @Bean
    public Consumer<MessageDto> reverse() {
        return v -> {
            String reversed = new StringBuilder(v.getMessage()).reverse().toString();
            MessageDto reversedDto = MessageDto.newBuilder()
                    .setMessage(reversed)
                    .build();
            saveMessage(reversedDto);
        };
    }

    private void saveMessage(MessageDto dto) {
        MessageEntity message = new MessageEntity();
        message.setMessage(dto.getMessage());
        this.repository.saveAndFlush(message);
    }
}

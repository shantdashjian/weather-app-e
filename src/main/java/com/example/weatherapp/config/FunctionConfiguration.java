package com.example.weatherapp.config;

import com.example.weatherapp.entity.MessageEntity;
import com.example.weatherapp.repository.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class FunctionConfiguration {

    MessageRepository repository;

    public FunctionConfiguration(MessageRepository repository) {
        this.repository = repository;
    }

    @Bean
    public Consumer<String> identity() {
        return v -> {
            System.out.println("Received: " + v);
            MessageEntity message = new MessageEntity();
            message.setMessage(v);
            this.repository.saveAndFlush(message);
        };
    }

    // Get the data from RabbitMQ
    // Send the data to reverse
    @Bean
    public Function<String, String> uppercase() {
        return v -> {
            String uppercased = v.toUpperCase();
            System.out.println(uppercased);
            return uppercased;
        };
    }

    // Get the data from uppercase
    // Store the data to file
    @Bean
    public Consumer<String> reverse() {
        return v -> {
            String reversed = new StringBuilder(v).reverse().toString();
            System.out.println("Reversed: " + reversed);
            MessageEntity message = new MessageEntity();
            message.setMessage(reversed);
            this.repository.saveAndFlush(message);
        };
    }

}

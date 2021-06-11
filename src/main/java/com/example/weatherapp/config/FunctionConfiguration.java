package com.example.weatherapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class FunctionConfiguration {

    FileWriter identityFileWriter;
    FileWriter reversedFileWriter;

    public FunctionConfiguration(FileWriter identityFileWriter, FileWriter reversedFileWriter) {
        this.identityFileWriter = identityFileWriter;
        this.reversedFileWriter = reversedFileWriter;
    }

    @Bean
    public Consumer<String> identity() {
        return v -> {
            System.out.println("Received: " + v);
            try {
                identityFileWriter.append(v);
                identityFileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    @Bean
    public Consumer<String> reverse() {
        return v -> {
            String reversed = new StringBuilder(v).reverse().toString();
            System.out.println("Reversed: " + reversed);
            try {
                reversedFileWriter.append(reversed);
                reversedFileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        };
    }

    // get the data from RabbitMQ
    // send the data to reverse
    @Bean
    public Function<String, String> uppercase() {
        return v -> {
            String uppercased = v.toUpperCase();
            System.out.println(uppercased);
            return uppercased;
        };
    }
}

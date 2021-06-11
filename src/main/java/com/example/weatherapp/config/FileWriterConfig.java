package com.example.weatherapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Configuration
@ConfigurationProperties(prefix = "file-properties")
public class FileWriterConfig {

    private String identityFilename;
    private String reversedFilename;

    public String getReversedFilename() {
        return reversedFilename;
    }

    public void setReversedFilename(String reversedFilename) {
        this.reversedFilename = reversedFilename;
    }

    public String getIdentityFilename() {
        return identityFilename;
    }

    public void setIdentityFilename(String identityFilename) {
        this.identityFilename = identityFilename;
    }

    @Bean
    public FileWriter identityFileWriter() throws IOException {
        File file = new File(identityFilename);
        return new FileWriter(file);
    }

    @Bean
    public FileWriter reversedFileWriter() throws IOException {
        File file = new File(reversedFilename);
        return new FileWriter(file);
    }

}

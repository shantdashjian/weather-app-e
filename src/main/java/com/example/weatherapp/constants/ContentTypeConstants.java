package com.example.weatherapp.constants;

import org.springframework.util.MimeType;

public enum ContentTypeConstants {
    AVRO_MIME_TYPE(MimeType.valueOf("application/*+avro"));

    private MimeType type;

    ContentTypeConstants(MimeType type) {
        this.type = type;
    }

    public MimeType getValue() {
        return this.type;
    }
}

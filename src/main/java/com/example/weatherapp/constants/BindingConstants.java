package com.example.weatherapp.constants;

public enum BindingConstants {
    IDENTITY_BINDING("identity-in-0"),
    REVERSE_BINDING("reverse-in-0"),
    UPPERCASE_BINDING("uppercase-in-0"),
    UPPERCASE_REVERSE_BINDING("upperCaseReverseInput");

    private String value;

    BindingConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

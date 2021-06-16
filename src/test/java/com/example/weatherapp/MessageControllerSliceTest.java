package com.example.weatherapp;

import com.example.weatherapp.publish.model.Message;
import com.example.weatherapp.publish.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class MessageControllerSliceTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MessageService service;

    private final ObjectMapper mapper = new ObjectMapper();

    private Message payload;

    @BeforeEach
    void setup() {
        payload = new Message();
        payload.setContent("hello");
    }

    @Test
    void pass_message_to_service_when_posting_to_identity_path() throws Exception {
        ResultActions result = mvc.perform(post("/message/identity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(payload))
        );

        result.andExpect(status().isOk());
        verify(service).publishToIdentity(any());
    }

    @Test
    void pass_message_to_service_when_posting_to_reverse_path() throws Exception {
        ResultActions result = mvc.perform(post("/message/reverse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(payload))
        );

        result.andExpect(status().isOk());
        verify(service).publishToReverse(any());
    }

    @Test
    void pass_message_to_service_when_posting_to_uppercase_path() throws Exception {
        ResultActions result = mvc.perform(post("/message/uppercase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(payload))
        );

        result.andExpect(status().isOk());
        verify(service).publishToUppercase(any());
    }

    @Test
    void pass_message_to_service_when_posting_to_uppercase_reverse_path() throws Exception {
        ResultActions result = mvc.perform(post("/message/uppercase-reverse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(payload))
        );

        result.andExpect(status().isOk());
        verify(service).publishToUppercaseReverse(any());
    }

}

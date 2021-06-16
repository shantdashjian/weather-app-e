package com.example.weatherapp.publish.service;

import com.example.weatherapp.publish.model.Message;
import com.example.weatherapp.schema.MessageDto;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import static com.example.weatherapp.constants.BindingConstants.*;
import static com.example.weatherapp.constants.ContentTypeConstants.AVRO_MIME_TYPE;

@Service
public class MessageService {

    private final StreamBridge streamBridge;

    public MessageService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishToIdentity(Message message) {
        streamBridge.send(IDENTITY_BINDING.getValue(), buildMessageDto(message), AVRO_MIME_TYPE.getValue());
    }

    public void publishToReverse(Message message) {
        streamBridge.send(REVERSE_BINDING.getValue(), buildMessageDto(message), AVRO_MIME_TYPE.getValue());
    }

    public void publishToUppercase(Message message) {
        streamBridge.send(UPPERCASE_BINDING.getValue(), buildMessageDto(message), AVRO_MIME_TYPE.getValue());
    }

    public void publishToUppercaseReverse(Message message) {
        streamBridge.send(UPPERCASE_REVERSE_BINDING.getValue(), buildMessageDto(message), AVRO_MIME_TYPE.getValue());
    }

    private MessageDto buildMessageDto(Message message) {
        return MessageDto.newBuilder().setMessage(message.getContent()).build();
    }
}

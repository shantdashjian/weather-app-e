package com.example.weatherapp.publish;

import com.example.weatherapp.publish.model.Message;
import com.example.weatherapp.schema.MessageDto;
import com.example.weatherapp.publish.service.PublisherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;

import static com.example.weatherapp.constants.BindingConstants.*;
import static com.example.weatherapp.constants.ContentTypeConstants.AVRO_MIME_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    private StreamBridge streamBridge = mock(StreamBridge.class);

    private PublisherService messageService = new PublisherService(streamBridge);

    private static ArgumentCaptor<MessageDto> messageDtoCaptor;

    private static Message message;

    @BeforeAll
    public static void setup() {
        messageDtoCaptor = ArgumentCaptor.forClass(MessageDto.class);
        message = new Message();
        message.setContent("hello");
    }

    @Test
    void publish_to_identity_topic() {
        messageService.publishToIdentity(message);
        verify(streamBridge).send(eq(IDENTITY_BINDING.getValue()), messageDtoCaptor.capture(), eq(AVRO_MIME_TYPE.getValue()));
        assertThat(messageDtoCaptor.getValue().getMessage()).isEqualTo("hello");
    }

    @Test
    void publish_to_reverse_topic() {
        messageService.publishToReverse(message);
        verify(streamBridge).send(eq(REVERSE_BINDING.getValue()), messageDtoCaptor.capture(), eq(AVRO_MIME_TYPE.getValue()));
        assertThat(messageDtoCaptor.getValue().getMessage()).isEqualTo("hello");
    }

    @Test
    void publish_to_uppercase_topic() {
        messageService.publishToUppercase(message);
        verify(streamBridge).send(eq(UPPERCASE_BINDING.getValue()), messageDtoCaptor.capture(), eq(AVRO_MIME_TYPE.getValue()));
        assertThat(messageDtoCaptor.getValue().getMessage()).isEqualTo("hello");
    }

    @Test
    void publish_to_uppercase_reverse_topic() {
        messageService.publishToUppercaseReverse(message);
        verify(streamBridge).send(eq(UPPERCASE_REVERSE_BINDING.getValue()), messageDtoCaptor.capture(), eq(AVRO_MIME_TYPE.getValue()));
        assertThat(messageDtoCaptor.getValue().getMessage()).isEqualTo("hello");
    }
}

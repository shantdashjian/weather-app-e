package com.example.weatherapp.publish.controller;

import com.example.weatherapp.publish.model.Message;
import com.example.weatherapp.publish.service.PublisherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class PublisherController {

    private final PublisherService messageService;

    public PublisherController(PublisherService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/identity")
    public void createMessage(@RequestBody Message message) {
        messageService.publishToIdentity(message);
    }

    @PostMapping("/reverse")
    public void reverseMessage(@RequestBody Message message) {
        messageService.publishToReverse(message);
    }

    @PostMapping("/uppercase")
    public void uppercaseMessage(@RequestBody Message message) {
        messageService.publishToUppercase(message);
    }

    @PostMapping("/uppercase-reverse")
    public void uppercaseReverseMessage(@RequestBody Message message) {
        messageService.publishToUppercaseReverse(message);
    }
}

package com.sherif.myapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ListFilesController {

    @Autowired
    private KafkaProducerService producerService;

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/send")
    public String sendMessage(@RequestBody Map<String, String> payload) {
        String username = payload.getOrDefault("username", "defaultUser");
        String messageContent = payload.getOrDefault("content", "");
        producerService.sendMessage("test-topic", username, messageContent);

        return "Message sent!";
    }
}


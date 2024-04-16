package com.sherif.myapi.controler;

import com.sherif.myapi.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ListFilesController {

    @Autowired
    private KafkaProducerService producerService;


    @PostMapping("/send")
    public String sendMessage(@RequestBody String payload) {
        producerService.sendMessage("test-topic", "message", payload);
        return "Message sent!" + payload;
    }
}


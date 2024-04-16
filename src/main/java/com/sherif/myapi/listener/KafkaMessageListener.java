package com.sherif.myapi.listener;

import com.sherif.myapi.service.KafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class KafkaMessageListener {

    @Autowired
    private KafkaConsumerService consumerService;

    @PostConstruct
    public void start() {
        consumerService.consumeMessages();
    }
}

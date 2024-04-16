package com.sherif.myapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
public class UserService {
    @Autowired
    MessageRepository messageRepository;
    public Flux<Message> getMessages() {
        return Flux.fromIterable(messageRepository.findAll());
    }


}

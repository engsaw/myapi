package com.sherif.myapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
public class ListFilesController {

    @Autowired
    private KafkaProducerService producerService;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper;

    public ListFilesController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.userService = userService;

    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody String payload) {
        producerService.sendMessage("test-topic", "message", payload);
        return "Message sent!";
    }


    @GetMapping(value = "/get", produces = "application/json")
    public Mono<ResponseEntity<String>> getMessage() {
        return userService.getMessages()
                .collectList()
                .flatMap(this::serializeMessages)
                .map(json -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error serializing messages: " + e.getMessage())));
    }



    private Mono<String> serializeMessages(List<Message> messages) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(messages))
                .onErrorMap(JsonProcessingException.class, e -> new RuntimeException("Error serializing messages", e));
    }
}


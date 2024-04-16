package com.sherif.myapi.service;

import com.sherif.myapi.model.Message;
import com.sherif.myapi.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import reactor.core.scheduler.Schedulers;

@Service
public class KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final KafkaReceiver<String, String> kafkaReceiver;
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Disposable subscription;

    @Autowired
    public KafkaConsumerService(KafkaReceiver<String, String> kafkaReceiver, MessageRepository messageRepository) {
        this.kafkaReceiver = kafkaReceiver;
        this.messageRepository = messageRepository;
    }

    private Mono<Void> processRecord(ReceiverRecord<String, String> record) {
        return Mono.fromCallable(() -> {
                    Message message;
                    try {
                        message = objectMapper.readValue(record.value(), Message.class);
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Error processing message: {}", record.value(), e);
                        throw new RuntimeException("Error processing message", e);
                    }
                    return message;
                })
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(messageRepository::save) // Using method reference
                .then(Mono.fromRunnable(record.receiverOffset()::acknowledge)) // Correctly return Mono<Void>
                .then(); // Ensure the final return type is Mono<Void>
    }




    @PostConstruct
    public void consumeMessages() {
        LOGGER.info("Starting to subscribe");
        subscription = kafkaReceiver.receive()
                .flatMap(record -> processRecord(record).onErrorResume(e -> Mono.empty()))
                .subscribe();
        LOGGER.info("Subscribed successfully");

    }

    @PreDestroy
    public void stop() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }


}

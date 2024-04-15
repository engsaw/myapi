package com.sherif.myapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.List;

@Service
public class KafkaConsumerService {

    @Autowired
    private KafkaReceiver<String, String> kafkaReceiver;

    @Autowired
    private MessageRepository messageRepository;

    public KafkaConsumerService(KafkaReceiver<String, String> kafkaReceiver, MessageRepository messageRepository) {
        this.kafkaReceiver = kafkaReceiver;
        this.messageRepository = messageRepository;
    }

    private Mono<Void> processBatch(List<ReceiverRecord<String, String>> batch) {
        return Flux.fromIterable(batch)
                .doOnNext(record -> {
                    System.out.println("Processed message: " + record.value());
                    // Assume record.value() is JSON string that includes a username
                    System.out.println(record);
                    Message message = new Message(record.key(), record.value());  // default username
                    messageRepository.save(message);  // Save to database
                    record.receiverOffset().acknowledge();
                })
                .then();
    }

    public void consumeMessages() {
        kafkaReceiver.receive()
                .buffer(100)
                .flatMap(this::processBatch)
                .cache()
                .subscribe();
    }
}

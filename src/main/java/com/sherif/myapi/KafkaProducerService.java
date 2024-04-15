package com.sherif.myapi;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaSender<String, String> kafkaSender;

    public void sendMessage(String topic, String key, String value) {
        kafkaSender.send(Mono.just(SenderRecord.create(new ProducerRecord<>(topic, key, value), null)))
                .doOnError(e -> System.err.println("Send failed: " + e))
                .subscribe(r -> System.out.println("Send successful"));
    }
}

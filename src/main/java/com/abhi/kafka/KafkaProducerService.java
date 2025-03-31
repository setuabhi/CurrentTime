package com.abhi.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; //<String, String> represents the key and value types of the Kafka messages being produced.

    public void sendMessage(String topic, String message) {
        //kafkaTemplate.send(topic, "Key", message); if you want to mention key to control partition
        kafkaTemplate.send(topic, message);
        System.out.println("Message sent to topic " + topic + ": " + message);
    }
}

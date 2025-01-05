package com.abhi.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }

    //will get same message what consume/consumeInSameGroup gets
    @KafkaListener(topics = "my-topic", groupId = "my-group-2")
    public void consumeGroup2(String message) {
        System.out.println("Consumed message from group - 2: " + message);
    }

    // either this or consume will get message since there is only 1 partition
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consumeInSameGroup(String message) {
        System.out.println("Consumed message by second consumer from group 1: " + message);
    }

    @KafkaListener(topics = "my-topic-2", groupId = "my-group")
    public void consumeFromTopic2Stream(String message) {
        System.out.println("Consumed message from Topic 2: " + message);
    }
}

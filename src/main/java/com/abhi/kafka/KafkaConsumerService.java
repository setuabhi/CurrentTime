package com.abhi.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consume(String message) {
        System.out.println("Consumed message my-topic my-group: " + message);
    }

    //will get same message what consume/consumeInSameGroup gets
    // since we have not provided details of my-group-2 in application.yml , spring will set default (key-deserializer and value-deserializer will be string)
    @KafkaListener(topics = "my-topic", groupId = "my-group-2")
    public void consume(ConsumerRecord<String, String> record) {
        System.out.println("Consumed message my-topic my-group2: " + record.value());
        System.out.println("Key: " + record.key());
        System.out.println("Partition: " + record.partition());
        System.out.println("Offset: " + record.offset());
    }

    // either this or consume will get message since there is only 1 partition
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consumeInSameGroup(String message) {
        System.out.println("Consumed message by second consumer from group 1: " + message);
    }

    @KafkaListener(topics = "my-topic-2", groupId = "my-group") //Message is coming from Stream
    public void consumeFromTopic2Stream(String message) {
        System.out.println("Consumed message from Topic 2: " + message);
    }
}

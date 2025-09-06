package com.abhi.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topicPartitions = @TopicPartition(topic = "my-topic", partitions = {"0"}), groupId = "my-group") // to control partition
    public void consume2(String message) {
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

    // either this or consume2 will get message since there is only 1 partition
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consumeInSameGroup(String message, Acknowledgment ack) {
        System.out.println("Consumed message by second consumer from group 1: " + message);
        // ✅ manually commit offset only after successful processing
        ack.acknowledge(); // set enable-auto-commit: false
    }

    @KafkaListener(topics = "my-topic-2", groupId = "my-group") //Message is coming from Stream
    public void consumeFromTopic2Stream(String message) {
        System.out.println("Consumed message from Topic 2: " + message);
    }
}

package com.abhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@EnableKafkaStreams
@SpringBootApplication
public class CurrentTimeMain {
    public static void main(String[] args) {
        SpringApplication.run(CurrentTimeMain.class,args);
    }
}

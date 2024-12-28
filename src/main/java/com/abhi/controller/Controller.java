package com.abhi.controller;

import com.abhi.kafka.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
public class Controller {

    @Autowired
    private KafkaProducerService producerService;

    @GetMapping("/current-usa-time")
    public String getCurrentUsaTime() {
        // Specify the desired timezone
        ZoneId usaZoneId = ZoneId.of("America/New_York");

        // Get the current time in the specified timezone
        LocalDateTime nowInUsa = LocalDateTime.now(usaZoneId);

        // Define the custom format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE h:mm a d'st' MMMM yyyy");

        // Replace the 'st' dynamically based on the day
        String formattedTime = nowInUsa.format(formatter);
        formattedTime = formattedTime.replaceAll("1st", "1st")
                .replaceAll("2st", "2nd")
                .replaceAll("3st", "3rd");

        return formattedTime;
    }

    @PostMapping("/publish")
    public String sendMessage(@RequestParam String topic, @RequestParam String message) {
        producerService.sendMessage(topic, message);
        return "Message sent to topic " + topic + ": " + message;
    }


}

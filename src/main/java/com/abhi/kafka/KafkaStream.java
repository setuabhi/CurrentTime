package com.abhi.kafka;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaStream {
    //KStream<String, String> bcoz key serde and value serde are Strings
    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder.stream("my-topic");
        stream.mapValues(value -> value.toUpperCase())
                .to("my-topic-2");
        return stream;
    }
}

package com.felix.kafkaexample.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

public class KafkaConfigs {

    private static Producer<String, String> producer;
    private static String topicName;
    static {
        init();
    }
    static void init() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);

        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");


        producer = new KafkaProducer<>(properties);
        topicName = "my-topic";
    }


    public static Producer<String, String> getProducer() {
        return producer;
    }

    public static String getTopicName() {
        return topicName;
    }
}

package com.felix.springkafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "some-another-topic")
    public void receiveMessage(ConsumerRecord<Integer, String> record) {
        LOGGER.info("Received key = {}, message = '{}'", record.key(), record.value());
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}

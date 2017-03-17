package com.felix.springkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener
    public void receiveMessage(String message) {
        LOGGER.info("Received message = '{}'", message);
        latch.countDown();
    }
}

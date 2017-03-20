package com.felix.springkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    @Value("${kafka.topic}")
    private String topic;

    private Integer key = 0;

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    public void sendMessage(String message) {

        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate
                .send(topic, key++, message);

        future.addCallback(
                new ListenableFutureCallback<SendResult<Integer, String>>() {

                    @Override
                    public void onSuccess(
                            SendResult<Integer, String> result) {
                        LOGGER.info("Sent key={}, message='{}' offset={}",
                                result.getProducerRecord().key(),
                                result.getProducerRecord().value(),
                                result.getRecordMetadata().offset());
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        LOGGER.error("unable to send message='{}'",
                                message, ex);
                    }
                });

    }
}

package com.felix.kafkaexample.producer;

public class Main {

    public static void main(String[] args) {
        KafkaTwitterProducer kafkaTwitterProducer = new KafkaTwitterProducer();
        try {
            kafkaTwitterProducer.initTwitter();
            kafkaTwitterProducer.sendMessages();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

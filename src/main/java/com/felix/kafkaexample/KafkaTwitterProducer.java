package com.felix.kafkaexample;

import twitter4j.Status;

import java.util.concurrent.LinkedBlockingQueue;

public class KafkaTwitterProducer {

    private TwitterConfigs twitterConfigs = new TwitterConfigs();
    private LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>();


}

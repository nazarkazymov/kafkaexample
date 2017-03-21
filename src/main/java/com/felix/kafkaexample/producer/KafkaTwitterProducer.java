package com.felix.kafkaexample.producer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.concurrent.LinkedBlockingQueue;

public class KafkaTwitterProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaTwitterProducer.class);

    private TwitterConfigs twitterConfigs = new TwitterConfigs();
    private KafkaConfigs kafkaConfigs = new KafkaConfigs();
    private LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>();

    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
    private TwitterStream twitterStream;

    public void initTwitter() throws InterruptedException {
        configurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterConfigs.CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterConfigs.CONSUMER_SECRET)
                .setOAuthAccessToken(TwitterConfigs.ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(TwitterConfigs.ACCESS_TOKEN_SECRET);


        twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
        StatusListener statusListener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                queue.offer(status);

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                LOGGER.info("Got a status deletion notice id:"
                        + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                LOGGER.info("Got track limitation notice:" +
                        numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                LOGGER.info("Got scrub_geo event userId:" + userId +
                        "upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                LOGGER.info("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(statusListener);

        FilterQuery query = new FilterQuery().track(TwitterConfigs.KEYWORDS.split(","));
        twitterStream.filter(query);
        Thread.sleep(5_000);
    }

    public void sendMessages() throws InterruptedException {
        Producer<String, String> producer = KafkaConfigs.getProducer();
        int i = 0;
        int j = 0;

        while (i < 100) {
            Status twitterStatus = queue.poll();

            if (twitterStatus == null) {
                Thread.sleep(1000);
                i++;
            } else {
                int key = j;
                String value = twitterStatus.getText();
                producer.send(new ProducerRecord<>(
                        KafkaConfigs.getTopicName(), Integer.toString(j++), value));
                LOGGER.info("Sending messsage with key = {}, value = '{}'", key, value);
            }
        }

        producer.close();
        Thread.sleep(2000);
        twitterStream.shutdown();
    }
}

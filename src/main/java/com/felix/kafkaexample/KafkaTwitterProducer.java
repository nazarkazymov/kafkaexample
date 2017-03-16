package com.felix.kafkaexample;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.concurrent.LinkedBlockingQueue;

public class KafkaTwitterProducer {

    private TwitterConfigs twitterConfigs = new TwitterConfigs();
    private KafkaConfigs kafkaConfigs = new KafkaConfigs();
    private LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>();

    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
    private TwitterStream twitterStream;

    void initTwitter() throws InterruptedException {
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
                System.out.println("@" + status.getUser().getScreenName()
                        + " - " + status.getText());
                for (URLEntity urle : status.getURLEntities()) {
                    System.out.println(urle.getDisplayURL());
                }

                for (HashtagEntity hashtag : status.getHashtagEntities()) {
                    System.out.println(hashtag.getText());
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:"
                        + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" +
                        numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId +
                        "upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                 System.out.println("Got stall warning:" + warning);
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
                Thread.sleep(100);
                i++;
            } else {
                for (HashtagEntity hashtag: twitterStatus.getHashtagEntities()) {
                    System.out.println("Hashtag: " + hashtag.getText());
                    producer.send(new ProducerRecord<>(
                            KafkaConfigs.getTopicName(), Integer.toString(j++), hashtag.getText()));
                }
            }
        }

        producer.close();
        Thread.sleep(2000);
        twitterStream.shutdown();
    }
}

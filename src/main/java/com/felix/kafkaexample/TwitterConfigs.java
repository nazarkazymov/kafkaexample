package com.felix.kafkaexample;

public class TwitterConfigs {

    public final static String CONSUMER_KEY = CommonUtils.getProperty("twitter.consumer.key");
    public final static String CONSUMER_SECRET = CommonUtils.getProperty("twitter.consumer.secret");
    public final static String ACCESS_TOKEN = CommonUtils.getProperty("twitter.access.token");
    public final static String ACCESS_TOKEN_SECRET = CommonUtils.getProperty("twitter.access.token.secret");
    public static final String KEYWORDS = CommonUtils.getProperty("twitter.keywords");
}

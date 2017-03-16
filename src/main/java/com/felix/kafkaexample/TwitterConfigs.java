package com.felix.kafkaexample;

public class TwitterConfigs {

    public final static String CONSUMER_KEY = CommonUtils.initProperties().getProperty("consumer_key");
    public final static String CONSUMER_SECRET = CommonUtils.initProperties().getProperty("consumer_secret");
    public final static String ACCESS_TOKEN = CommonUtils.initProperties().getProperty("access_token");
    public final static String ACCESS_TOKEN_SECRET = CommonUtils.initProperties().getProperty("access_token_secret");
}

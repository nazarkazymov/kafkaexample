package com.felix.kafkaexample;

import java.io.IOException;
import java.util.Properties;

public class CommonUtils {

    static {
        initProperties("config.properties");
    }

    static private Properties properties;

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    static Properties initProperties(String fileName) {
        properties = new Properties();
        try {
            properties.load(CommonUtils.class.getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}

package com.felix.kafkaexample;

import java.io.IOException;
import java.util.Properties;

public class CommonUtils {

    static Properties initProperties() {
        Properties properties = new Properties();
        try {
            properties.load(CommonUtils.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}

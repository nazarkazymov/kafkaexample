package com.felix.springkafka;


import com.felix.springkafka.util.MemoryMonitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaApplicationTest {

    @Autowired
    private Sender sender;

    @Autowired
    private Receiver receiver;

    private MemoryMonitor memoryMonitor;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @Before
    public void init(){
        memoryMonitor = new MemoryMonitor();
    }

    @Test
    public void testKafkaReceiver() throws Exception {

        Runnable task = () -> {
            String message = memoryMonitor.monitor();
            sender.sendMessage(message);

        };
        scheduler.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);
        Thread.currentThread().join();
        receiver.getLatch().await(1, TimeUnit.MINUTES);
        assertThat(receiver.getLatch().getCount()).isEqualTo(0);
    }
}

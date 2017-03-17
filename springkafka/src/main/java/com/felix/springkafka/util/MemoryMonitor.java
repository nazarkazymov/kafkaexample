package com.felix.springkafka.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.concurrent.*;

public class MemoryMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryMonitor.class);
    NumberFormat format = NumberFormat.getInstance();
    BlockingQueue<String> chunk = new LinkedBlockingQueue<>();

    public BlockingQueue<String> getChunk() {
        return chunk;
    }

    public String monitor() {
        Runtime runtime = Runtime.getRuntime();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append("free memory: " + format.format(freeMemory / 1024) + "/n");
        sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "/n");
        sb.append("max memory: " + format.format(maxMemory / 1024) + "/n");
        sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "/n");
        chunk.offer(sb.toString());
        return sb.toString();
    }

    public MemoryMonitor() {
        monitor();
    }

}

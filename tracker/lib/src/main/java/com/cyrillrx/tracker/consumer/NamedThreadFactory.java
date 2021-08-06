package com.cyrillrx.tracker.consumer;

import java.util.concurrent.ThreadFactory;

/**
 * @author Cyril Leroux
 *         Created on 18/02/16.
 */
public class NamedThreadFactory implements ThreadFactory {

    private final String prefix;
    private int threadCount;

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
        this.threadCount = 0;
    }

    @Override
    public Thread newThread(Runnable r) { return new Thread(r, generateName()); }

    private String generateName() { return prefix + threadCount; }
}

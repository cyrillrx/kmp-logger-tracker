package com.cyrillrx.tracker.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author Cyril Leroux
 *         Created on 23/04/2016.
 */
public class Utils {

    public static void wait(long timeDuration, TimeUnit timeUnit) {

        try {
            timeUnit.sleep(timeDuration);
        } catch (InterruptedException e) {
            // TODO log error
        }
    }
}

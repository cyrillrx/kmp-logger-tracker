package com.cyrillrx.tracker.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author Cyril Leroux
 *         Created on 23/04/2016.
 */
object Utils {
    fun wait(timeDuration: Long, timeUnit: java.util.concurrent.TimeUnit) {
        try {
            timeUnit.sleep(timeDuration)
        } catch (e: java.lang.InterruptedException) {
            // TODO log error
        }
    }
}

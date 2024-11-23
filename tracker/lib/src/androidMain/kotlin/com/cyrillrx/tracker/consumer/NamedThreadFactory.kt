package com.cyrillrx.tracker.consumer

import java.util.concurrent.ThreadFactory

/**
 * @author Cyril Leroux
 * Created on 18/02/16.
 */
class NamedThreadFactory(private val prefix: String) : ThreadFactory {
    private val threadCount = 0

    override fun newThread(r: Runnable): Thread {
        return Thread(r, generateName())
    }

    private fun generateName(): String {
        return prefix + threadCount
    }
}

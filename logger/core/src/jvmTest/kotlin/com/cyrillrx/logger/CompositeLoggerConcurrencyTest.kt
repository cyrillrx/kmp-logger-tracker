package com.cyrillrx.logger

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompositeLoggerConcurrencyTest {
    @Test
    fun `logging while children are added and removed never fails`() {
        val logger = CompositeLogger()
        val stable = RamLogChild()
        logger.add(stable)
        val failures = mutableListOf<Throwable>()
        val executor = Executors.newFixedThreadPool(THREADS)
        val start = CountDownLatch(1)

        repeat(THREADS) { thread ->
            executor.execute {
                start.await()
                repeat(ITERATIONS) {
                    runCatching {
                        if (thread % 2 == 0) {
                            val child = RamLogChild()
                            logger.add(child)
                            logger.remove(child)
                        } else {
                            logger.info(TAG) { "message" }
                        }
                    }.onFailure { synchronized(failures) { failures += it } }
                }
            }
        }
        start.countDown()
        executor.shutdown()

        assertTrue(executor.awaitTermination(30, TimeUnit.SECONDS))
        assertEquals(emptyList(), failures)
        assertEquals(THREADS / 2 * ITERATIONS, stable.entries().size)
    }

    private companion object {
        const val TAG = "CompositeLoggerConcurrencyTest"
        const val THREADS = 8
        const val ITERATIONS = 1_000
    }
}

package com.cyrillrx.logger.extension

import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Cyril Leroux
 *         Created on 15/01/2019.
 */
class CrashlyticsLoggerTest {

    @Test
    fun testIsoDateFormatter() {

        val dateTimePattern = CrashlyticsLogger.ISO_DATE_TIME
        val simpleDateFormat = SimpleDateFormat(dateTimePattern, Locale.US)
        val isoFormattedDate = simpleDateFormat.format(Date(0L))

        Assert.assertEquals("Wrong date formatting.",
                "1970-01-01T01:00:00+0100[CET]",
                isoFormattedDate)
    }
}
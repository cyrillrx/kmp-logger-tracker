package com.cyrillrx.companion.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.logger.Logger
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.extension.LogCat
import com.cyrillrx.logger.extension.SystemOutLog
import com.cyrillrx.tracker.Tracker
import com.cyrillrx.tracker.TrackerChild
import com.cyrillrx.tracker.event.TrackEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.addChild(SystemOutLog(Severity.DEBUG))
        Logger.addChild(LogCat(Severity.DEBUG, true))

        Logger.info("MainActivity", "Enter")

        Tracker.setupExceptionCatcher { t -> Logger.error("Tracker", "Caught exception", t) }
        Tracker.addChild(createTracker())
        val event = TrackEvent.Builder()
            .setName("DummyEvent")
            .build()

        Tracker.track(event)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingView("Hello Logger and Tracker")
                }
            }
        }
    }

    private fun createTracker() = object : TrackerChild("dummy_tracker") {
        override fun doTrack(event: TrackEvent) {
            Logger.info("MainActivity", "Tracking event: $event")
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}

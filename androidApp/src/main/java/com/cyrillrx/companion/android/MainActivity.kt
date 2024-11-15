package com.cyrillrx.companion.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.logger.Logger
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.extension.SystemOutLog
import com.cyrillrx.tracker.Tracker
import com.cyrillrx.tracker.TrackerChild
import com.cyrillrx.tracker.event.TrackEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.initialize()
        Logger.addChild(SystemOutLog(Severity.DEBUG))
        Logger.info("Android MainActivity", "Enter")

        Tracker.initialize()
        Tracker.addChild(createTracker())
        val event = TrackEvent.Builder()
            .setName("Dummy")
            .setCategory("test")
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

    private fun createTracker() = object : TrackerChild() {
        override fun doTrack(event: TrackEvent?) {
            TODO("Not yet implemented")
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

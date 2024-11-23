package com.cyrillrx.tracker.consumer

import com.cyrillrx.tracker.event.TrackEvent

abstract class EventConsumer<EventContainer : Collection<TrackEvent>>(
    protected val events: EventContainer
) : Runnable {

    protected var running: Boolean = false

    override fun run() {
        running = true
        while (running) {
            consume()
        }
    }

    protected abstract fun consume()

    class StopEvent : TrackEvent("STOP_EVENT")
}

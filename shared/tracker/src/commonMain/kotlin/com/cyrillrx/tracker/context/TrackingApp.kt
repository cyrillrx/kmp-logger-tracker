package com.cyrillrx.tracker.context

class TrackingApp(val name: String, val version: String) {
    companion object {
        val UNDEFINED = TrackingApp(name = "undefined", version = "undefined")
    }
}

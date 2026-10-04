package com.cyrillrx.tracker.event

import com.cyrillrx.tracker.context.TrackerContext
import com.cyrillrx.tracker.event.TrackEvent.Builder.Companion.requireNotBlank
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
open class TrackEvent(
    val name: String,
    val attributes: MutableMap<String, Any> = HashMap(),
) {
    lateinit var context: TrackerContext

    /** Convenience method for iOS */
    constructor(name: String) : this(name = name, attributes = HashMap())

    init {
        requireNotBlank(name) { "Event name is mandatory." }
    }

    /** Validates that an event is ready to be sent. */
    fun isValid(): Boolean = try {
        require(::context.isInitialized) { "TrackerContext is mandatory but was not set." }
        true
    } catch (exception: Exception) {
        false
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getAttribute(key: String?): T? = attributes[key] as? T

    fun getAttributesAsDictionary(): Map<String, Any> = attributes

    fun addAttribute(key: String, value: Any) {
        attributes[key] = value
    }

    fun addAttributes(attributes: Map<String, Any>) {
        this.attributes.putAll(attributes)
    }

    class Builder {
        private var name: String? = null
        private val attributes = HashMap<String, Any>()

        fun build(): TrackEvent {
            val name = requireNotBlank(name) { "Event name is mandatory." }

            return TrackEvent(name = name, attributes = attributes)
        }

        fun setName(name: String) = apply { this.name = name }

        fun addAttribute(key: String, value: Any) = apply { attributes[key] = value }

        fun addAttributes(block: MutableMap<String, Any>.() -> Unit) = apply { attributes.apply(block) }

        fun <T : Any> addAttributes(values: Map<String, T>) = apply { attributes.putAll(values) }

        companion object {
            /**
             * Throws an [IllegalArgumentException] with the result of calling [lazyMessage]
             * if the [value] is null. Otherwise
             * returns the not null value.
             */
            @OptIn(ExperimentalContracts::class)
            inline fun requireNotBlank(value: String?, lazyMessage: () -> Any): String {
                contract {
                    returns() implies (value != null)
                }

                return if (value.isNullOrBlank()) {
                    val message = lazyMessage()
                    throw IllegalArgumentException(message.toString())
                } else {
                    value
                }
            }
        }
    }
}

package com.cyrillrx.logger

/**
 * @author Cyril Leroux
 *         Created on 26/10/2016.
 */
interface ExceptionCatcher {

    fun catchException(t: Throwable)
}
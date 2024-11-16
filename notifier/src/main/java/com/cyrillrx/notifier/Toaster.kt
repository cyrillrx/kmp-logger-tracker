package com.cyrillrx.notifier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;
import androidx.annotation.StringRes

/**
 * Util that relies on the app context to toast messages to the user.
 * It also provides a developer toast feature displayed at the top of the screen (dev-mode only).
 *
 * @author Cyril Leroux
 *         Created on 29/03/16
 */
object Toaster {

    private var debugToast: Toast? = null
    private lateinit var userToast: Toast

    @SuppressLint("ShowToast")
    fun initUserToast(context: Context) {
        userToast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
    }

    @SuppressLint("ShowToast")
    fun initDeveloperToast(context: Context) {
        debugToast = Toast.makeText(context, "", Toast.LENGTH_LONG)
            .apply { setGravity(Gravity.TOP, 0, 50) }
    }

    @Synchronized
    fun showMessage(message: String) {
        toastLong(message)
    }

    @Synchronized
    fun showMessage(@StringRes messageRes: Int) {
        toastLong(messageRes)
    }

    @Synchronized
    fun showDevMessage(message: String) {
        debugToast?.show(message)
    }

    @Synchronized
    fun showDevMessage(@StringRes messageRes: Int) {
        debugToast?.show(messageRes)
    }

    /**
     * Shows a classical toast (for the user).
     * @param message The message to toast.
     */
    @Synchronized
    private fun toastShort(message: String) {
        userToast.duration = Toast.LENGTH_SHORT
        userToast.show(message)
    }

    /**
     * Shows a classical toast (for the user).
     * @param messageRes The resource of the message to toast.
     */
    @Synchronized
    private fun toastShort(@StringRes messageRes: Int) {
        userToast.duration = Toast.LENGTH_SHORT
        userToast.show(messageRes)
    }

    /**
     * Shows a classical toast (for the user).
     * @param message The message to toast.
     */
    @Synchronized
    private fun toastLong(message: String) {
        userToast.duration = Toast.LENGTH_LONG
        userToast.show(message)
    }

    /**
     * Shows a classical toast (for the user).
     * @param messageRes The resource of the message to toast.
     */
    @Synchronized
    private fun toastLong(@StringRes messageRes: Int) {
        userToast.duration = Toast.LENGTH_LONG
        userToast.show(messageRes)
    }

    /**
     * Shows a toast.
     * @param message The message to toast.
     */
    private fun Toast.show(message: String) {
        setText(message)
        show()
    }

    /**
     * Shows a toast.
     * @param messageRes The resource of the message to toast.
     */
    private fun Toast.show(@StringRes messageRes: Int) {
        setText(messageRes)
        show()
    }
}

package com.cyrilleroux.android.toolbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author Cyril Leroux
 *         Created 21/12/2014.
 */
public class Logger {

    private static final String TAG = Logger.class.getSimpleName();

    private static Toast sDebugToast;
    private static boolean sIsDebug;

    @SuppressLint("ShowToast")
    public static void initialize(Context context, boolean isDebug) {
        sIsDebug = isDebug;

        if (!sIsDebug) { return; }

        sDebugToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        sDebugToast.setGravity(Gravity.TOP, 0, 50);
    }

    /**
     * Shows a debug toast.
     *
     * @param message The message to toast.
     */
    public static void toast(String message) {
        if (!sIsDebug) { return; }

        if (sDebugToast != null) {
            sDebugToast.setText(message);
            sDebugToast.show();
        } else {
            android.util.Log.e(TAG, "toast() - Debug Toast hasn't been initialized ! ");
        }
    }

    public static void verbose(String tag, String message) {
        if (sIsDebug) { android.util.Log.v(tag, message); }
    }

    public static void info(String tag, String message) {
        if (sIsDebug) { android.util.Log.i(tag, message); }
    }

    public static void warning(String tag, String message) {
        if (sIsDebug) { android.util.Log.w(tag, message); }
    }

    public static void warning(String tag, String message, Throwable throwable) {
        if (sIsDebug) { android.util.Log.w(tag, message, throwable); }
    }

    public static void error(String tag, String message) {
        if (sIsDebug) { android.util.Log.e(tag, message); }
    }

    public static void error(String tag, String message, Throwable throwable) {
        if (sIsDebug) { android.util.Log.e(tag, message, throwable); }
    }
}

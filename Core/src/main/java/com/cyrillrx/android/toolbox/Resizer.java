package com.cyrillrx.android.toolbox;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Uses a ViewTreeObserver to resize a view just in time
 *
 * @author Cyril Leroux
 *         Created on 16/12/2014
 */
public class Resizer {

    private static final String TAG = Resizer.class.getSimpleName();

    /**
     * Resizes the height of a view using the passed aspect ratio.
     *
     * @param view  The view to resize
     * @param ratio The aspect ratio to apply.
     */
    public static void resizeHeight(final View view, final float ratio) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        view.getLayoutParams().height = (int) (view.getWidth() / ratio);
                        view.requestLayout();

                        try {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } catch (IllegalStateException e) {
                            Log.w(TAG, "IllegalStateException while removing the observer.", e);
                        }
                    }
                });
    }
}
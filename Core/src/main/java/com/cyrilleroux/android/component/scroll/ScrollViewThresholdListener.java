package com.cyrilleroux.android.component.scroll;

import android.widget.ScrollView;

/**
 * @author Cyril Leroux
 *         Created on 22/01/2015
 */
public abstract class ScrollViewThresholdListener implements OnScrollChangedListener, ScrollDirectionListener {

    private int mLastScrollY;
    private int mScrollThreshold;

    @Override
    public void onScrollChanged(ScrollView who, int x, int y, int oldX, int oldY) {
        boolean isSignificantDelta = Math.abs(y - mLastScrollY) > mScrollThreshold;
        if (isSignificantDelta) {
            if (y > mLastScrollY) {
                onScrollUp();
            } else {
                onScrollDown();
            }
        }
        mLastScrollY = y;
    }

    public void setThreshold(int scrollThreshold) { mScrollThreshold = scrollThreshold; }
}
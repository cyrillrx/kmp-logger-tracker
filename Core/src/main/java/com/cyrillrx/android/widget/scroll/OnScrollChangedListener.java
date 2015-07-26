package com.cyrillrx.android.widget.scroll;

import android.widget.ScrollView;

/**
 * @author Cyril Leroux
 *         Created on 22/11/2014
 */
public interface OnScrollChangedListener {

    void onScrollChanged(ScrollView scrollView, int x, int y, int oldX, int oldY);
}
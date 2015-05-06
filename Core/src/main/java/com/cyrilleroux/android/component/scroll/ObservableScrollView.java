package com.cyrilleroux.android.component.scroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author Cyril Leroux
 *         Created on 22/11/2014
 */
public class ObservableScrollView extends ScrollView {

    protected OnScrollChangedListener mScrollListener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mScrollListener = listener;
    }

    public OnScrollChangedListener getOnScrollChangedListener() {
        return mScrollListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (mScrollListener != null) {
            mScrollListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }
}

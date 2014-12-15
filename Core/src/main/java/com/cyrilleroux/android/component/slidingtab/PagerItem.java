package com.cyrilleroux.android.component.slidingtab;

import android.support.v4.app.Fragment;

/**
 * Created on 29/10/14.
 */
public class PagerItem<PageFragment extends Fragment> {

    private final CharSequence mTitle;
    private final int mIndicatorColor;
    private final int mDividerColor;
    private final PageFragment mFragment;

    private PagerItem(CharSequence title, int indicatorColor, int dividerColor, PageFragment fragment) {
        mTitle = title;
        mIndicatorColor = indicatorColor;
        mDividerColor = dividerColor;
        mFragment = fragment;
    }

    public CharSequence getTitle() { return mTitle; }

    public int getIndicatorColor() { return mIndicatorColor; }

    public int getDividerColor() { return mDividerColor; }

    public PageFragment getFragment() { return mFragment; }
}
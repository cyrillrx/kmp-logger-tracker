package com.cyrilleroux.android.sample;

/**
 * POJO that represents a Sample view / activity
 * Created on 02/10/2014.
 */
public class Sample {

    private String mTitle;
    private String mSubtitle;
    private Class mClazz;

    public Sample(String title, String subtitle, Class clazz) {
        mTitle = title;
        mSubtitle = subtitle;
        mClazz = clazz;
    }

    public String getTitle() { return mTitle; }

    public String getSubtitle() { return mSubtitle; }

    public Class getClazz() { return mClazz; }
}

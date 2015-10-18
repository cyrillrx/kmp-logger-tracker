package com.cyrillrx.android.section;

/**
 * Wraps both headers and items in order to display them in a recycler view.
 *
 * @author Cyril Leroux
 *         Created on 25/07/2015.
 */
public class ItemWrapper<Data> {

    private String mHeader;
    private Data mData;

    /** Constructor that wraps data. */
    public ItemWrapper(String header, Data data) {
        mHeader = header;
        mData = data;
    }

    /** Constructor that wraps data. */
    public ItemWrapper(Data data) {
        mHeader = null;
        mData = data;
    }

    /** Constructor that wraps a header. */
    public ItemWrapper(String header) { mHeader = header; }

    public String getHeader() { return mHeader; }

    public Data getData() { return mData; }

    public void setData(Data data) { mData = data; }

    public boolean isHeader() { return mData == null; }
}

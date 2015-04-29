package com.cyrilleroux.android.linker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Link a data set to a view.
 *
 * @author Cyril Leroux
 *         Created on 04/12/14
 */
public class UniqueDataLinker<Data, Context> {

    protected final Set<DataLinkedView<Data, Context>> mLinkedViews;
    protected boolean mDataChanged;
    protected Data mData;
    protected ViewLinkedCallback mCallback;

    /**
     * Initializes the linker with the view to update.
     *
     * @param views The views that will be updated when setData is called.
     */
    public UniqueDataLinker(DataLinkedView<Data, Context>... views) {
        mLinkedViews = new HashSet<>();
        Collections.addAll(mLinkedViews, views);
    }

    /**
     * Sets or updates the parent with the given data.
     * Then refresh the view if necessary.
     *
     * @param data The new data.
     * @return True if data has changed.
     */
    public boolean setData(Data data, Context context) { return setData(data, null, null); }

    /**
     * Sets or updates the parent with the given data.
     * Then refresh the view if necessary.
     * The listener passed in parameter is called back when the view is loaded/refreshed
     *
     * @param data The new data.
     * @return True if data has changed.
     */
    public boolean setData(Data data, Context context, ViewLinkedCallback callback) {

        mDataChanged = data != null && !data.equals(mData);
        mData = data;
        mCallback = callback;

        refreshViews(data, context);

        return mDataChanged;
    }

    /**
     * Refresh the views linked to the component using {@link #addLinkedView(DataLinkedView)}.
     *
     * @param data The new data.
     */
    protected final void refreshViews(Data data, Context context) {
        if (!mDataChanged) {
            return;
        }

        for (DataLinkedView<Data, Context> view : mLinkedViews) {
            view.bind(data, context);
        }

        mDataChanged = false;

        if (mCallback != null) {
            mCallback.onLinked();
            // Call only once
            mCallback = null;
        }
    }

    public void onStartLoading() {
        for (DataLinkedView<Data, Context> view : mLinkedViews) {
            view.onStartLoading();
        }
    }

    public void onStopLoading() {
        for (DataLinkedView<Data, Context> view : mLinkedViews) {
            view.onStopLoading();
        }
    }

    public void onRequestFailure() {
        for (DataLinkedView<Data, Context> view : mLinkedViews) {
            view.onRequestFailure();
        }
    }

    public Data getData() { return mData; }

    /**
     * Add a linked view that will be updated when setData is called.
     *
     * @param linkedView
     * @return
     */
    public boolean addLinkedView(DataLinkedView<Data, Context> linkedView) {
        return mLinkedViews.add(linkedView);
    }

    public boolean removeLinkedView(DataLinkedView<Data, Context> linkedView) {
        return mLinkedViews.remove(linkedView);
    }
}

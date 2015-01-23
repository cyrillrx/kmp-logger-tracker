package com.cyrilleroux.android.ws;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Link the ws response to the view.
 * Created on 04/12/14.
 */
public class WsLinker<Data> {

    protected final Set<WsLinkedView<Data>> mLinkedViews;
    protected boolean mDataChanged;
    protected Data mData;
    protected OnViewLoadedListener mViewLoadedListener;

    /**
     * Initializes the linker with the view to update.
     *
     * @param views The views that will be updated when setData is called.
     */
    public WsLinker(WsLinkedView<Data>... views) {
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
    public boolean setData(Data data) { return setData(data, null); }

    /**
     * Sets or updates the parent with the given data.
     * Then refresh the view if necessary.
     * The listener passed in parameter is called back when the view is loaded/refreshed
     *
     * @param data The new data.
     * @return True if data has changed.
     */
    public boolean setData(Data data, OnViewLoadedListener listener) {

        mDataChanged = data != null && !data.equals(mData);
        mData = data;
        mViewLoadedListener = listener;

        refreshViews(data);

        return mDataChanged;
    }

    /**
     * Refresh the views linked to the component using {@link #addLinkedView(WsLinkedView)}.
     *
     * @param data The new data.
     */
    protected final void refreshViews(Data data) {
        if (!mDataChanged) {
            return;
        }

        for (WsLinkedView<Data> view : mLinkedViews) {
            view.bind(data);
        }

        mDataChanged = false;

        if (mViewLoadedListener != null) {
            mViewLoadedListener.onViewLoaded();
            // Call only once
            mViewLoadedListener = null;
        }
    }

    public void onStartLoading() {
        for (WsLinkedView<Data> view : mLinkedViews) {
            view.onStartLoading();
        }
    }

    public void onStopLoading() {
        for (WsLinkedView<Data> view : mLinkedViews) {
            view.onStopLoading();
        }
    }

    public Data getData() { return mData; }

    /**
     * Add a linked view that will be updated when setData is called.
     *
     * @param linkedView
     * @return
     */
    public boolean addLinkedView(WsLinkedView<Data> linkedView) {
        return mLinkedViews.add(linkedView);
    }

    public boolean removeLinkedView(WsLinkedView<Data> linkedView) {
        return mLinkedViews.remove(linkedView);
    }
}

package com.cyrillrx.android.linker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Link a data set to a view.
 *
 * @author Cyril Leroux
 *         Created on 04/12/14
 */
public class DataLinker<Data> {

    protected final Set<DataLinkedView<Data>> mLinkedViews;

    /**
     * Initializes the linker with the view to update.
     *
     * @param views The views that will be updated when setData is called.
     */
    public DataLinker(DataLinkedView<Data>... views) {
        mLinkedViews = new HashSet<>();
        Collections.addAll(mLinkedViews, views);
    }

    /**
     * Calls {@link #bindData(Object, ViewLinkedCallback)} with a null callback.
     *
     * @param data The new data.
     * @return True if the data has been bound.
     */
    public boolean bindData(Data data) { return bindData(data, null); }

    /**
     * Binds the given data to the linked views then trigger the callback if any.
     *
     * @param data The new data.
     * @return True if the data has been bound.
     */
    public boolean bindData(Data data, ViewLinkedCallback callback) {

        for (DataLinkedView<Data> view : mLinkedViews) {
            view.bind(data);
        }

        if (callback != null) {
            callback.onLinked();
        }

        return true;
    }

    /** Start loading data (from a database, a webservice, etc.). */
    public void onStartLoading() {
        for (DataLinkedView<Data> view : mLinkedViews) {
            view.onStartLoading();
        }
    }


    /** Stop loading data. */
    public void onStopLoading() {
        for (DataLinkedView<Data> view : mLinkedViews) {
            view.onStopLoading();
        }
    }

    /** Failure while loading data. */
    public void onLoadingFailure() {
        for (DataLinkedView<Data> view : mLinkedViews) {
            view.onRequestFailure();
        }
    }

    /**
     * Add a linked view that will be updated when {@link #bindData(Object, ViewLinkedCallback)} is called.
     *
     * @param linkedView The linked view to add.
     * @return True if the linked view  set is modified, false otherwise.
     */
    public boolean addLinkedView(DataLinkedView<Data> linkedView) {
        return mLinkedViews.add(linkedView);
    }

    public boolean removeLinkedView(DataLinkedView<Data> linkedView) {
        return mLinkedViews.remove(linkedView);
    }
}

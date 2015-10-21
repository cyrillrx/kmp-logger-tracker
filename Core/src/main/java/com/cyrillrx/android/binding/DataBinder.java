package com.cyrillrx.android.binding;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Link a data set to a view.
 *
 * @author Cyril Leroux
 *         Created on 04/12/14
 */
public class DataBinder<Data> {

    protected final Set<DataLinkedView<Data>> mLinkedViews;

    /**
     * Initializes an empty data binder.
     */
    public DataBinder() { mLinkedViews = new HashSet<>(); }

    /**
     * Initializes the binder with the view to update.
     *
     * @param views The views that will be updated when setData is called.
     */
    public DataBinder(DataLinkedView<Data>... views) {
        this();
        addLinkedViews(views);
    }

    /**
     * Calls {@link #bindData(Object, ViewBoundCallback)} with a null callback.
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
    public boolean bindData(Data data, ViewBoundCallback callback) {

        for (DataLinkedView<Data> view : mLinkedViews) {
            view.bind(data);
        }

        if (callback != null) {
            callback.onBound();
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
     * Add a linked view that will be updated when {@link #bindData(Object, ViewBoundCallback)} is called.
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

    /**
     * Add a list of linked view that will be updated when {@link #bindData(Object, ViewBoundCallback)} is called.
     *
     * @return True if the linked view set is modified, false otherwise.
     */
    public boolean addLinkedViews(DataLinkedView<Data>... views) {
        return Collections.addAll(mLinkedViews, views);
    }

}

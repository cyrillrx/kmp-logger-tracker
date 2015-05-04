package com.cyrilleroux.android.linker;

/**
 * Represents a view linked to a data object.
 *
 * @author Cyril Leroux
 *         Created on 04/12/14
 */
public interface DataLinkedView<Data> {

    /**
     * Binds the data to the view.
     *
     * @param data The data to bind.
     */
    void bind(Data data);

    void onStartLoading();

    void onStopLoading();

    void onRequestFailure();
}

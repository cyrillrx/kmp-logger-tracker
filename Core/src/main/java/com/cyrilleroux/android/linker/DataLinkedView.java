package com.cyrilleroux.android.linker;

/**
 * Represents a view linked to a data object.
 *
 * @author Cyril Leroux
 *         Created on 04/12/14
 */
public interface DataLinkedView<Data, Context> {

    /**
     * Binds the data to the view.
     *
     * @param data    The data to bind.
     * @param context Contextual information about the data.
     */
    void bind(Data data, Context context);

    void onStartLoading();

    void onStopLoading();

    void onRequestFailure();
}

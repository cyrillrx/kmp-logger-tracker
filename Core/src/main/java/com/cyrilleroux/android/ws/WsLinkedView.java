package com.cyrilleroux.android.ws;

/**
 * Implement in ws linked views.
 * Created on 04/12/14.
 */
public interface WsLinkedView<Data> {

    /**
     * Binds the data to the view.
     *
     * @param data The data to bind.
     */
    void bind(Data data);

    void onStartLoading();

    void onStopLoading();
}

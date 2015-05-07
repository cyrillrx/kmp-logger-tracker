package com.cyrillrx.android.ws;


/**
 * @deprecated use {@link com.cyrillrx.android.linker.DataLinkedView}
 */
@Deprecated
public interface WsLinkedView<Data> {

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

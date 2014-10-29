package com.cyrilleroux.android.network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cyrilleroux.android.core.toolbox.CompletionListener;

/**
 * Convenient parent for Fragments that receive a WS response
 * <p/>
 * Created on 29/10/14.
 */
public abstract class WsFragment<WsResponse> extends Fragment {

    protected boolean mDataChanged;
    protected WsResponse mResponse;
    protected CompletionListener mViewLoadedListener;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshView();
    }

    /**
     * Sets or updates the fragment with the given data.
     *
     * @param response The new data.
     */
    public void setData(WsResponse response) { setData(response, null); }

    /**
     * Sets or updates the fragment with the given data.
     *
     * @param response The new data.
     */
    public void setData(WsResponse response, CompletionListener listener) {
        mResponse = response;
        mDataChanged = true;
        mViewLoadedListener = listener;
    }

    /**
     * Refreshes the view if the data have changed
     */
    public final void refreshView() {
        if (!mDataChanged) {
            return;
        }

        bindWsResponse(mResponse);

        mDataChanged = false;

        if (mViewLoadedListener != null) {
            mViewLoadedListener.onCompleted();
        }
    }

    /**
     * Binds the WS response to the view.
     * Called after onActivityCreated (it is safe to call getActivity in this method).
     *
     * @param response The Ws response
     */
    protected abstract void bindWsResponse(WsResponse response);

}

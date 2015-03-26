package com.cyrilleroux.android.ws;

import android.support.v7.widget.RecyclerView;

/**
 * @author Cyril Leroux
 *         Created on 17/03/15
 */
public abstract class SimpleLoaderAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected static final int TYPE_LOADER = 100;

    protected boolean mIsLoading;

    public abstract void addLoader();

    public abstract void removeLoader();

    public boolean isLoader(int position) { return getItemViewType(position) == TYPE_LOADER; }
}

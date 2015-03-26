package com.cyrilleroux.android.ws;

import android.support.v7.widget.RecyclerView;

/**
 * An adapter to browse previous and next items.
 *
 * @author Cyril Leroux
 *         Created on 26/03/15
 */
public abstract class FullPaginationAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected static final int TYPE_LOADER = 100;

    public abstract void addPrevLoader();

    public abstract void removePrevLoader();

    public abstract void addNextLoader();

    public abstract void removeNextLoader();

    public boolean isLoader(int position) { return getItemViewType(position) == TYPE_LOADER; }
}

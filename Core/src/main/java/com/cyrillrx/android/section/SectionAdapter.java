package com.cyrillrx.android.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cyrillrx.android.toolbox.NamedList;
import com.cyrillrx.android.toolbox.OnDataClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Parent for section adapters.
 *
 * @author Cyril Leroux
 *         Created on 28/07/2015.
 */
public abstract class SectionAdapter<Data, VH extends SectionAdapter.SectionViewHolder<Data>>
        extends RecyclerView.Adapter<VH> {

    protected static final int ITEM_TYPE_NORMAL = 0;
    protected static final int ITEM_TYPE_HEADER = 1;
    protected static final int ITEM_TYPE_DIVIDER = 2;

    protected final List<ItemWrapper<Data>> mWrappers;
    protected final OnDataClickListener<Data> mOnDataClickListener;

    public static class SectionViewHolder<Data> extends RecyclerView.ViewHolder {

        protected TextView tvTitle;

        public SectionViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(ItemWrapper<Data> wrapper, OnDataClickListener<Data> clickListener) {
            if (wrapper.isHeader()) {
                bindHeader(wrapper.getHeader());
            } else {
                bindData(wrapper.getData(), clickListener);
            }
        }

        protected void bindData(final Data data, final OnDataClickListener<Data> clickListener) {
            // Handle data click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onDataClick(data);
                }
            });
        }

        protected void bindHeader(String header) {
            tvTitle.setText(header);
            // Reset click
            itemView.setOnClickListener(null);
        }
    }

    public SectionAdapter(OnDataClickListener<Data> clickListener) {
        mWrappers = new ArrayList<>();
        mOnDataClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(mWrappers.get(position), mOnDataClickListener);
    }

    @Override
    public int getItemCount() { return mWrappers.size(); }

    /** Populates the adapter using a collection of named lists. */
    public void populate(Collection<NamedList<Data>> namedLists) {

        int oldLength = mWrappers.size();

        String header;
        for (NamedList<Data> namedList : namedLists) {
            header = namedList.getTitle();
            mWrappers.add(new ItemWrapper<Data>(header));
            final List<Data> items = namedList.getItems();
            for (Data item : items) {
                mWrappers.add(new ItemWrapper<>(header, item));
            }
        }

        int newLength = mWrappers.size();
        notifyItemRangeInserted(oldLength, newLength - oldLength);
    }

    public void clear() {
        mWrappers.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mWrappers.get(position).isHeader() ? ITEM_TYPE_HEADER : ITEM_TYPE_NORMAL;
    }

    public ItemWrapper<Data> getWrapper(int position) {
        return mWrappers.get(position);
    }
}

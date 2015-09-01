package com.cyrillrx.android.toolbox;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Cyril Leroux
 *         Created on 30/08/2015.
 */
public class NamedList<Data> {

    private final String mTitle;
    private final List<Data> mItems;

    public NamedList(String title) {
        mTitle = title;
        mItems = new ArrayList<>();
    }

    public String getTitle() { return mTitle; }

    public List<Data> getItems() { return mItems; }

    public void add(Data data) { mItems.add(data); }

    public void addAll(@Nullable Collection<? extends Data> data) {
        if (data != null) { mItems.addAll(data); }
    }

    public void addAll(@Nullable Data[] data) {
        if (data != null) { mItems.addAll(Arrays.asList(data)); }
    }
}
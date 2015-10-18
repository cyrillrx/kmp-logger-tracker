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

    private final String title;
    private final List<Data> items;

    public NamedList(String title) {
        this.title = title;
        items = new ArrayList<>();
    }

    public NamedList(String title, List<Data> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() { return title; }

    public List<Data> getItems() { return items; }

    public Data getItem(int index) { return items.get(index); }

    public void add(Data data) { items.add(data); }

    public void addAll(@Nullable Collection<? extends Data> data) {
        if (data != null) { items.addAll(data); }
    }

    public void addAll(@Nullable Data[] data) {
        if (data != null) { items.addAll(Arrays.asList(data)); }
    }
}
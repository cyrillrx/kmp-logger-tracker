package com.cyrillrx.android.binding;

/**
 * Link a single data to a view.<br />
 * The linked data can be updated which refreshes the view.
 *
 * @author Cyril Leroux
 *         Created on 04/12/14
 */
public class SingleDataBinder<Data> extends DataBinder<Data> {

    protected boolean mDataChanged;
    protected Data mData;

    /**
     * Sets or updates the parent with the given data.
     * Then refresh the view if necessary.
     * The listener passed in parameter is called back when the view is loaded/refreshed
     *
     * @param data The new data.
     * @return True if data has changed.
     */
    @Override
    public boolean bindData(Data data, ViewBoundCallback callback) {

        mDataChanged = data != null && !data.equals(mData);
        mData = data;

        refreshViews(data, callback);

        return mDataChanged;
    }

    /**
     * Refresh the views linked to the component using {@link #addLinkedView(DataLinkedView)}.
     *
     * @param data The new data.
     */
    private void refreshViews(Data data, ViewBoundCallback callback) {
        if (!mDataChanged) {
            return;
        }

        for (DataLinkedView<Data> view : mLinkedViews) {
            view.bind(data);
        }

        // Reset the data changed state
        mDataChanged = false;

        if (callback != null) {
            callback.onBound();
        }
    }

    public Data getData() { return mData; }
}

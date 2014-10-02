package com.cyrilleroux.android.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created on 02/10/2014.
 */
public class SampleAdapter extends ArrayAdapter<Sample> {

    public SampleAdapter(Context context, List<Sample> objects) {
        super(context, 0, objects);
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvSubtitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);

            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(android.R.id.text1);
            holder.tvSubtitle = (TextView) convertView.findViewById(android.R.id.text2);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Sample sample = getItem(position);
        holder.tvTitle.setText(sample.getTitle());
        holder.tvSubtitle.setText(sample.getSubtitle());

        return convertView;
    }
}

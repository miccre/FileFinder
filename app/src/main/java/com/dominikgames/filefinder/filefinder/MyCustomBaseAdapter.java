package com.dominikgames.filefinder.filefinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dominik on 04.06.2017.
 */

public class MyCustomBaseAdapter extends BaseAdapter {
    private static ArrayList<SearchResult> searchArrayList;

    private LayoutInflater mInflater;

    public MyCustomBaseAdapter(Context context, ArrayList<SearchResult> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_view, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            holder.txtUrl = (TextView) convertView.findViewById(R.id.url);
            holder.txtSize = (TextView) convertView.findViewById(R.id.size);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(searchArrayList.get(position).getName());
        holder.txtUrl.setText(searchArrayList.get(position).getUrl());
        holder.txtSize.setText(String.valueOf(searchArrayList.get(position).getSize())+ " bytov");

        return convertView;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtUrl;
        TextView txtSize;
    }
}

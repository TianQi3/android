package com.humming.asc.sales.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Zhtq on 1/9/16.
 */
public abstract class AbstractArrayAdapter<TypeViewHolder,TypeRowData> extends ArrayAdapter {
    private int resource;

    public AbstractArrayAdapter(Context context, int resource,
                                List<TypeRowData> items) {
        super(context, resource, items);
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        TypeViewHolder holder = null;
        if (rowView == null) {
            LayoutInflater inflater = ((Activity) getContext())
                    .getLayoutInflater();
            rowView = inflater.inflate(resource, parent, false);

            holder = createViewHolder();
            initViewHolder(position, holder, rowView);
            rowView.setTag(holder);
        } else {
            holder = (TypeViewHolder) rowView.getTag();
        }
        TypeRowData rowData = (TypeRowData) getItem(position);
        setItemData(position, holder, rowData);
        return rowView;
    }

    protected abstract TypeViewHolder createViewHolder();

    protected abstract void initViewHolder(int position, TypeViewHolder viewHolder, View rowView);

    protected abstract void setItemData(int position, TypeViewHolder viewHolder, TypeRowData itemData);

}

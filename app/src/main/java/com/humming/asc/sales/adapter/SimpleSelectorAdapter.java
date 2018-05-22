package com.humming.asc.sales.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.humming.asc.sales.R;

import java.util.List;

public class SimpleSelectorAdapter extends ArrayAdapter<String> {
	private Context context;
	private int resource;

	public SimpleSelectorAdapter(Context context,
			List<String> objects) {
		super(context, R.layout.list_item_simple_selector, objects);
		this.context = context;
		this.resource = R.layout.list_item_simple_selector;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ItemHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context)
					.getLayoutInflater();
			row = inflater.inflate(resource, parent, false);

			holder = new ItemHolder();
			holder.text = (TextView) row
					.findViewById(R.id.list_item_simple_selector__text);
			row.setTag(holder);
		} else {
			holder = (ItemHolder) row.getTag();
		}

		String text = getItem(position);
		holder.text.setText(text);
		return row;
	}

	class ItemHolder {
		TextView text;
	}

}

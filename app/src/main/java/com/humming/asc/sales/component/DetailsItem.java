package com.humming.asc.sales.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.asc.sales.R;

/**
 * Created by Zhtq on 16/1/18.
 */
public class DetailsItem extends LinearLayout {
    private final TextView labelTV;
    private final ImageView iconFIV;
    private String label;
    public DetailsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.list_item_customer_details,this);
        labelTV = (TextView) findViewById(R.id.label_tv);
        iconFIV = (ImageView) findViewById(R.id.label_iv);
        TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.plans_daily_call_details_item);
        Drawable icon = a.getDrawable(R.styleable.plans_daily_call_details_item_icons);
        label = a.getString(R.styleable.plans_daily_call_details_item_labels);
        a.recycle();
        iconFIV.setImageDrawable(icon);
        labelTV.setText(label);
    }
    public void setBottomText(String label) {
        this.label = label;
        labelTV.setText(label);
    }

    public void setImg(Drawable icon) {
        iconFIV.setImageDrawable(icon);
    }
}

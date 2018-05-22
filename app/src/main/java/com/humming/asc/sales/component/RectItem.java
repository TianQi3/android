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
 * Created by PuTi(编程即菩提) on 12/29/15.
 */
public class RectItem extends LinearLayout {
    private final TextView labelTV;
    private final ImageView iconFIV;
    private final TextView topLabelTV;
    private final TextView topValueTV;
    private final TextView bottomLabelTV;
    private final TextView bottomValueTV;
    private String label;
    private String topLabel;
    private String topValue;
    private String bottomLabel;
    private String bottomValue;

    public RectItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.component_main_plans_rect_item, this);
        iconFIV = (ImageView) findViewById(R.id.icon);
        labelTV = (TextView) findViewById(R.id.label);
        topLabelTV = (TextView) findViewById(R.id.topLabel);
        topValueTV = (TextView) findViewById(R.id.topValue);
        bottomLabelTV = (TextView) findViewById(R.id.bottomLabel);
        bottomValueTV = (TextView) findViewById(R.id.bottomValue);
        TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.component_main_plans_rect_item);

        Drawable icon = a.getDrawable(R.styleable.component_main_plans_rect_item_iconSrc);
        label = a.getString(R.styleable.component_main_plans_rect_item_label);
        topLabel = a.getString(R.styleable.component_main_plans_rect_item_topLabel);
        topValue = a.getString(R.styleable.component_main_plans_rect_item_topValue);
        bottomLabel = a.getString(R.styleable.component_main_plans_rect_item_bottomLabel);
        bottomValue = a.getString(R.styleable.component_main_plans_rect_item_bottomValue);
        a.recycle();
        iconFIV.setImageDrawable(icon);
        labelTV.setText(label);
        topLabelTV.setText(topLabel);
        topValueTV.setText(topValue);
        bottomLabelTV.setText(bottomLabel);
        bottomValueTV.setText(bottomValue);
    }

    public void setLabel(String label) {
        this.label = label;
        labelTV.setText(label);
    }

    public void setTopLabel(String topLabel) {
        this.topLabel = topLabel;
        topLabelTV.setText(topLabel);
    }

    public void setTopValue(String topValue) {
        this.topValue = topValue;
        topValueTV.setText(topValue);
    }

    public void setBottomLabel(String bottomLabel) {
        this.bottomLabel = bottomLabel;
        bottomLabelTV.setText(bottomLabel);
    }

    public void setBottomValue(String bottomValue) {
        this.bottomValue = bottomValue;
        bottomValueTV.setText(bottomValue);
    }
}

package com.humming.asc.sales.component.main.plans;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.asc.sales.R;
import com.humming.asc.sales.model.KeyValue;

import java.util.List;

/**
 * Created by PuTi(编程即菩提) on 1/1/16.
 */
public class PlanChart extends LinearLayout {
    private final TextView label1;
    private final TextView label2;
    private final TextView label3;
    protected View view;
    protected PieChart pieChart;
    protected TextView value1;
    protected TextView value2;
    protected TextView value3;

    public PlanChart(Context context) {
        this(context, null);
    }

    public PlanChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.component_plans_chart, this);
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        label1 = (TextView) view.findViewById(R.id.pie_chart_label1);
        label2 = (TextView) view.findViewById(R.id.pie_chart_label2);
        label3 = (TextView) view.findViewById(R.id.pie_chart_label3);
        value1 = (TextView) view.findViewById(R.id.item_completed_value);
        value2 = (TextView) view.findViewById(R.id.item_canceled_value);
        value3 = (TextView) view.findViewById(R.id.item_planned_value);
    }

    public void setData(int total, List<KeyValue> keyValues) {
        String[] keys = new String[3];
        int[] values = new int[3];
        for (int i = 0; i < 3; i++) {
            KeyValue keyValue = keyValues.get(i);
            keys[i] = keyValue.getKey();
            values[i] = keyValue.getValue();
        }
        // int totals = values[0]+values[1]+values[2];
        pieChart.setData(total, values[0], values[1], values[2]);
        label1.setText(keys[0]);
        label2.setText(keys[1]);
        label3.setText(keys[2]);
        value1.setText(String.valueOf(values[0]));
        value3.setText(String.valueOf(values[1]));
        value2.setText(String.valueOf(values[2]));
    }
}

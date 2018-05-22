package com.humming.asc.sales.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.asc.sales.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PuTi(编程即菩提) on 12/29/15.
 */
public class PageIndicator extends LinearLayout implements ViewPager.OnPageChangeListener,TabLayout.OnTabSelectedListener,View.OnClickListener {
    private final Drawable drawableIndicator;
    private final Drawable drawableIndicatorCurrent;
    private List<View> views = new ArrayList<View>();
    private View lastView;

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawableIndicator = ContextCompat.getDrawable(context, R.drawable.indicator);
        drawableIndicatorCurrent = ContextCompat.getDrawable(context,R.drawable.indicator_current);
    }

    public void setCount(int count){
        this.removeAllViews();
        views.clear();

        Context context = this.getContext();
        int size = context.getResources().getDimensionPixelSize(R.dimen.indicator_size);
        int margin = context.getResources().getDimensionPixelSize(R.dimen.indicator_margin);
        for(int i = 0;i<count;i++){
            TextView tv = new TextView(context);
            LayoutParams layoutParams = new LayoutParams(size, size);
            layoutParams.setMargins(margin,0,margin,0);
            tv.setLayoutParams(layoutParams);
            tv.setOnClickListener(this);
            addView(tv);
            tv.setBackground(drawableIndicator);
            views.add(tv);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(positionOffsetPixels != 0){
            return;
        }
        if(this.lastView!= null){
            this.lastView.setBackground(drawableIndicator);
        }
        View view = this.lastView = views.get(position);
        view.setBackground(drawableIndicatorCurrent);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        Log.v("xxxxx","click");
    }
}

package com.humming.asc.sales.component;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Zhtq on 1/7/16.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private final List<View> listView;

    public ViewPagerAdapter(List<View> viewList) {
        super();
        this.listView = viewList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View x = listView.get(position);
        collection.addView(x, 0);
        return x;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) listView.get(position));
    }

    @Override
    public int getCount() {
        return listView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}

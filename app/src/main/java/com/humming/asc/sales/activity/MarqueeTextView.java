package com.humming.asc.sales.activity;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Zhtq on 16/3/17.
 */
public class MarqueeTextView extends TextView
{
    public MarqueeTextView(Context context)
    {
        super(context);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(Integer.MAX_VALUE);
    }

    @Override
    public boolean isFocused()
    {
        return true;
    }
}

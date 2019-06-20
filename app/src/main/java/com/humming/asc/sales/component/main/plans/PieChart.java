package com.humming.asc.sales.component.main.plans;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.humming.asc.sales.R;

/**
 * Created by Zhtq on 12/31/15.
 */
public class PieChart extends View {
    private int completedColor;
    private String label = "Daily Call";
    private final int arcBackgroundColor;
    private int plannedColor;
    private int canceledColor;
    private int completed = 0;
    private int planned = 0;
    private int canceled = 0;
    private int total = 0;

    private Paint arcPaint;
    private int cx;
    private int cy;
    private int radius;
    private int stockWidth = 80;
    private float completedAng = 0;
    private float plannedAng;
    private float canceledAng;
    private float spAng = 0;

    private final Rect textBounds = new Rect();
    private Paint textPaint;
    private RectF canvasRect;

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PieChart,
                0, 0);
        try {
            arcBackgroundColor = a.getColor(R.styleable.PieChart_arcBackgrounColor, ContextCompat.getColor(context, R.color.arcBackgroundColor));
            completedColor = a.getColor(R.styleable.PieChart_completedColor, ContextCompat.getColor(context, R.color.completedColor));
            plannedColor = a.getColor(R.styleable.PieChart_plannedColor, ContextCompat.getColor(context, R.color.plannedColor));
            canceledColor = a.getColor(R.styleable.PieChart_canceledColor, ContextCompat.getColor(context, R.color.canceledColor));
        } finally {
            a.recycle();
        }
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

//        int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);
        cx = w / 2;
        cy = h / 2;
        radius = Math.min(cx, cy) - stockWidth;
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAng = 0;

        arcPaint.setStyle(Paint.Style.STROKE);
        canvasRect.set(cx - radius, cy - radius, cx + radius, cy + radius);

        if (total == 0) {
            arcPaint.setColor(arcBackgroundColor);
            canvas.drawOval(canvasRect, arcPaint);
        } else {
            arcPaint.setColor(completedColor);
            canvas.drawArc(canvasRect, startAng, completedAng, false, arcPaint);
            startAng = startAng + spAng + completedAng;
            arcPaint.setColor(plannedColor);
            canvas.drawArc(canvasRect, startAng, plannedAng, false, arcPaint);
            startAng = startAng + spAng + plannedAng;
            arcPaint.setColor(canceledColor);
            canvas.drawArc(canvasRect, startAng, canceledAng, false, arcPaint);
        }

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(55);
        String text = String.valueOf(this.total);
        textPaint.getTextBounds(text, 0, text.length(), textBounds);

        canvas.drawText(String.valueOf(total), cx, cy - textBounds.exactCenterY() - textBounds.height() / 2, textPaint);

        textPaint.setTextSize(25);
        text = this.label;
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx, cy - textBounds.exactCenterY() + textBounds.height(), textPaint);

//        canvas.drawOval(new RectF(cx -2,cy-2,cx+2,cy+2),arcPaint);

    }

    public void setData(int total, int completed, int planned, int canceled) {
        // this.completed = completed == 0? 1:completed;
        //   this.planned = planned == 0? 1:planned;
        // this.canceled = canceled==0? 1:canceled;
        this.completed = completed;
        this.planned = planned;
        this.canceled = canceled;
//        this.total = this.canceled+this.planned+this.canceled;
        this.total = total;
        if (total == 0) {
            completedAng = 0;
            plannedAng = 0;
            canceledAng = 0;
        } else {
            if (this.completed == 0) {
                completedAng = 0;
            } else {
                completedAng = 360 * this.completed / total;
            }
            if (this.planned == 0) {
                plannedAng = 0;
            } else {
                plannedAng = 360 * this.planned / total;
            }
            if (this.canceled == 0) {
                canceledAng = 0;
            } else {
                canceledAng = 360 - completedAng - plannedAng;
                //   canceledAng = 360 * this.canceled / total;
            }
        }

        spAng = 0;
        invalidate();
        requestLayout();
    }

    private void init() {
        arcPaint = new Paint();
        arcPaint.setColor(Color.BLUE);
        arcPaint.setStrokeWidth(stockWidth);
        arcPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        canvasRect = new RectF();
    }
}

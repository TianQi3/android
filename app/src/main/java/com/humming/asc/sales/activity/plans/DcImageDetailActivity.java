package com.humming.asc.sales.activity.plans;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.component.SmoothImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Zhtq on 16/3/10.
 */

public class DcImageDetailActivity extends AbstractActivity {

    private ArrayList<String> mDatas;
    private int mPosition;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    SmoothImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");
        mPosition = getIntent().getIntExtra("position", 0);
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);
        ImageLoader.getInstance().displayImage(mDatas.get(mPosition), imageView);
		//imageView.setImageResource(R.drawable.temp);
         ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f,
         1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
         0.5f);
         scaleAnimation.setDuration(300);
         scaleAnimation.setInterpolator(new AccelerateInterpolator());
         imageView.startAnimation(scaleAnimation);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }
        });
        imageView.transformOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

}

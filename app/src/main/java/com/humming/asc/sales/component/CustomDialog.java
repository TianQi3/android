package com.humming.asc.sales.component;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.humming.asc.sales.R;

/**
 * 自定义透明的dialog
 */
public class CustomDialog extends Dialog {
    private String content;

    public CustomDialog(Context context, String content) {
        super(context, R.style.CustomDialog);
        this.content = content;
        initView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (CustomDialog.this.isShowing())
                    //    CustomDialog.this.dismiss();
                    break;
        }
        return true;
    }

    private void initView() {
        setContentView(R.layout.dialog_view);
        ((TextView) findViewById(R.id.tvcontent)).setText(content);
        ImageView image = (ImageView) findViewById(R.id.progress_image);
        Glide.with(getContext())
                .load(R.drawable.loading_one)
                .into(image);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = 0.9f;
        getWindow().setAttributes(attributes);
        setCancelable(false);
    }
}
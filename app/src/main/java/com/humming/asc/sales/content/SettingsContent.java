package com.humming.asc.sales.content;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.settings.MyDraftsActivity;
import com.humming.asc.sales.activity.settings.MySettingActivity;
import com.humming.asc.sales.activity.settings.PositionListActivity;
import com.humming.asc.sales.activity.settings.UserMangerActivity;

/**
 * Created by Zhtq on 1/7/16.
 */
public class SettingsContent extends LinearLayout {

    private final View view;
    private TextView positionValue;
    private View user, drafts, setting, position;

    public SettingsContent(Context context) {
        this(context, null);
    }

    public SettingsContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_settings, this);
        user = findViewById(R.id.fragment_settings_user);
        drafts = findViewById(R.id.fragment_settings_mymails);
        setting = findViewById(R.id.fragment_settings_setting);
        position = findViewById(R.id.fragment_settings__position);
        positionValue = (TextView) findViewById(R.id.position_value);
        user.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), UserMangerActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);

            }
        });
        drafts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MyDraftsActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);

            }
        });
        setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MySettingActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);

            }
        });
        position.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), PositionListActivity.class));
            }
        });
    }

    public void setPosition(String name) {
        positionValue.setText(name);
    }
}

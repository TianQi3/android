package com.humming.asc.sales.content;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.LoginActivity;
import com.humming.asc.sales.activity.settings.MyDraftsActivity;
import com.humming.asc.sales.activity.settings.PositionListActivity;
import com.humming.asc.sales.activity.settings.UserMangerActivity;

import java.util.Locale;

/**
 * Created by PuTi(编程即菩提) on 1/7/16.
 */
public class SettingsContent extends LinearLayout {

    private final View view;
    private TextView userName, userName2, languageText, positionValue;
    private View user, drafts, language, position;

    public SettingsContent(Context context) {
        this(context, null);
    }

    public SettingsContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_settings, this);
        user = findViewById(R.id.fragment_settings_user);
        drafts = findViewById(R.id.fragment_settings_mymails);
        language = findViewById(R.id.fragment_settings_language);
        position = findViewById(R.id.fragment_settings__position);
        userName = (TextView) findViewById(R.id.fragment_settings_username);
        userName2 = (TextView) findViewById(R.id.fragment_settings_usernames);
        languageText = (TextView) findViewById(R.id.change_language_text);
        positionValue = (TextView) findViewById(R.id.position_value);
        userName.setText(Config.USERNAME);
        userName2.setText(Config.USERNAME);
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
        language.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale curLocal = getResources().getConfiguration().locale;
                Configuration config = getResources().getConfiguration();//获取系统的配置
                String currentLanguage = "";
                if (curLocal.equals(Locale.SIMPLIFIED_CHINESE)) {
                    config.locale = Locale.ENGLISH;//将语言更改为简体中文
                    currentLanguage = "chinese";
                } else {
                    config.locale = Locale.SIMPLIFIED_CHINESE;//将语言更改为英文
                    currentLanguage = "english";
                }
                SharedPreferences preferences = Application.getInstance().getSharedPreferences("language", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("currentLanguage", currentLanguage);
                editor.commit();
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());//更新配置
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), LoginActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                Application.getInstance().getCurrentActivity().finish();
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

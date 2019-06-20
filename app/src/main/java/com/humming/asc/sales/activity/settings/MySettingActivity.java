package com.humming.asc.sales.activity.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.LoginActivity;
import com.humming.asc.sales.activity.MainActivity;

import java.util.Locale;

public class MySettingActivity extends AbstractActivity {
    private View changeLanguageCn, changeLanguageEn;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.tab_settings));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logout = (Button) findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.activity.finish();
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        changeLanguageCn = findViewById(R.id.fragment_settings_language_cn);
        changeLanguageEn = findViewById(R.id.fragment_settings_language_en);
        changeLanguageCn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
        changeLanguageEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builer = new AlertDialog.Builder(MySettingActivity.this);
        builer.setTitle(Application.getInstance().getCurrentActivity().getResources().getString(R.string.remind));
        builer.setMessage(Application.getInstance().getCurrentActivity().getResources().getString(R.string.change));
        builer.setPositiveButton(getResources().getString(R.string.determine), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = getSharedPreferences("language", Activity.MODE_PRIVATE);
                String currentLanguage = preferences.getString("currentLanguage", "");
                // Configuration config = getResources().getConfiguration();//获取系统的配置
                if ("".equals(currentLanguage)) {
                    Locale curLocal = getResources().getConfiguration().locale;
                    if ("zh".equals(curLocal.getLanguage())) {
                        changeLanguages("english");
                    } else {
                        changeLanguages("chinese");
                    }
                } else {
                    Log.v("xxxxx", "++++++" + currentLanguage);
                    if ("chinese".equals(currentLanguage)) {
                        changeLanguages("english");
                    } else {
                        changeLanguages("chinese");
                    }
                    //   getResources().updateConfiguration(config, getResources().getDisplayMetrics());//更新配置
                }
                        /*Locale curLocal = getResources().getConfiguration().locale;
                        Configuration config = getResources().getConfiguration();//获取系统的配置
                        String currentLanguage = "";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (curLocal.equals(Locale.SIMPLIFIED_CHINESE)) {
                                config.setLocale(Locale.ENGLISH);//8.0以上使用
                                currentLanguage = "chinese";
                            } else {
                                config.setLocale(Locale.SIMPLIFIED_CHINESE);//将语言更改为英文
                                currentLanguage = "english";
                            }
                        } else {
                            if (curLocal.equals(Locale.SIMPLIFIED_CHINESE)) {
                                config.locale = Locale.ENGLISH;//将语言更改为简体中文
                                currentLanguage = "chinese";
                            } else {
                                config.locale = Locale.SIMPLIFIED_CHINESE;//将语言更改为英文
                                currentLanguage = "english";
                            }
                        }
                        SharedPreferences preferences = Application.getInstance().getSharedPreferences("language", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("currentLanguage", currentLanguage);
                        editor.commit();
                        getResources().updateConfiguration(config, getResources().getDisplayMetrics());//更新配置
                        MainActivity.activity.finish();
                        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), LoginActivity.class);
                        Application.getInstance().getCurrentActivity().startActivity(intent);
                        Application.getInstance().getCurrentActivity().finish();*/
            }
        });

        builer.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builer.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void changeLanguages(String currentLanguage) {
        String currentLanguages = currentLanguage;
        SharedPreferences preferencess = Application.getInstance().getSharedPreferences("language", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editors = preferencess.edit();
        editors.putString("currentLanguage", currentLanguages);
        editors.commit();

        Locale myLocale;
        if ("english".equals(currentLanguage)) {
            myLocale = Locale.ENGLISH;
        } else {
            myLocale = Locale.SIMPLIFIED_CHINESE;
        }
        Configuration configuration = this.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= 19) {
            configuration.setLocale(myLocale);
        } else {
            configuration.locale = myLocale;
        }
        new WebView(Application.getInstance().getApplicationContext()).destroy();
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        getResources().updateConfiguration(configuration, displayMetrics);
        Application.getInstance().finishAllActivity();
        Intent intent2 = new Intent(Application.getInstance().getCurrentActivity(), LoginActivity.class);
        // Intent intent3 = new Intent(Application.getInstance().getCurrentActivity(), MySettingActivity.class);
        startActivity(intent2);
        // startActivity(intent3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_customer_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

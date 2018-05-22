package com.humming.asc.sales.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.humming.asc.dp.presentation.vo.AuthVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.MenuResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.InfoService;

import java.util.Locale;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AbstractActivity implements ICallback<AuthVO> {
    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private long mExitTime; //退出时间
    private View mLoginFormView;
    private Button mSignInButton;
    public static final String SETTING_INFOS = "setting_infos";
    public static final String NAME = "NAME";
    public static final String PASSWORD = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断语言
        SharedPreferences preferences = getSharedPreferences("language", Activity.MODE_PRIVATE);
        String currentLanguage = preferences.getString("currentLanguage", "");
        if (currentLanguage == null || "".equals(currentLanguage)) {

        } else {
            Configuration config = getResources().getConfiguration();//获取系统的配置
            if ("chinese".equals(currentLanguage)) {
                config.locale = Locale.ENGLISH;//将语言更改为简体中文
            } else {
                config.locale = Locale.SIMPLIFIED_CHINESE;//将语言更改为英文
            }
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());//更新配置
        }
        setContentView(R.layout.activity_login);
        // Set up the login form.

        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences(SETTING_INFOS, 0);
        String userName = sharedPreferences.getString(NAME, "");
        String passWord = sharedPreferences.getString(PASSWORD, "");
        mUsernameView.setText(userName);
        mPasswordView.setText(passWord);
//        mLoginFormView = findViewById(R.id.frame_login);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isEmailValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_employee_id));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Application.getInstance().getAuthService().login(this, username, password);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
//        return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
//        return password.length() > 4;
        return true;
    }

    private void showProgress(final boolean show) {
        if (show) {
            mUsernameView.setEnabled(false);
            mPasswordView.setEnabled(false);
            mSignInButton.setEnabled(false);
            mLoading.show();
        } else {
            mUsernameView.setEnabled(true);
            mPasswordView.setEnabled(true);
            mSignInButton.setEnabled(true);
            mLoading.hide();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(SETTING_INFOS, 0);
        sharedPreferences.edit().putString(NAME, mUsernameView.getText().toString()).putString(PASSWORD, mPasswordView.getText().toString()).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(this, Application.getInstance().getString(R.string.application_exit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDataReady(AuthVO vo) {

        Application.setUserId(vo.getUserid());
        if (vo.getState() == 1) {
            Application.setIsLeadSale(vo.getIsLeadSale());
            InfoService infoService = Application.getInfoService();
            infoService.query(new ICallback<MenuResultVO>() {
                @Override
                public void onDataReady(MenuResultVO data) {
                    Config.USERNAME = mUsernameView.getText().toString();
                    Intent intent = new Intent(Application.getInstance().getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(Throwable throwable) {

                }
            }, "daily");
        } else if (vo.getState() == 3) {
            showProgress(false);
            mPasswordView.setError(getString(R.string.error_login_failed));
            mPasswordView.requestFocus();
        } else if (vo.getState() == 5) {
            showProgress(false);
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        } else {
            showProgress(false);
            mPasswordView.setError(getString(R.string.error_login_failed));
            mPasswordView.requestFocus();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        showProgress(false);
    }

}


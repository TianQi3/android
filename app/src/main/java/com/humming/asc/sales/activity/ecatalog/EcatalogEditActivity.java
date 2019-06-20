package com.humming.asc.sales.activity.ecatalog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.CreateEcatalogRequest;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.model.BackRefreshOneEvent;
import com.humming.asc.sales.model.ecatalog.CreateEcatalogEntity;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.ecatalog.CreateEcatalogResponse;
import com.humming.dto.ecatalogResponse.user.UserResponse;
import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

public class EcatalogEditActivity extends AbstractActivity implements View.OnClickListener, View.OnTouchListener {

    private ImageView back;
    private EditText mCreateTitle;
    private EditText mCreateCustomerName;
    private EditText mCreateContacts;
    private TextView mCreateExprotContract;
    private EditText mCreateContactPhone;
    private ImageView mCreateUserImg;
    private TextView mCreateUserName;
    private TextView mCreateUserPhone;
    private TextView mCreateCreate;
    private TextView mScopeTv;
    private LinearLayout dateView;
    private TextView dateTv;
    private static final int PICK_CONTACT = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private Intent mIntent;
    private int year, monthOfYear, dayOfMonth;
    private TextView titleTv;
    private CreateEcatalogEntity ecatalogEntity = null;
    private LinearLayout scopeView;
    private CustomDialog customDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecatalog_create);
        Bundle resultBundle = getIntent().getExtras();
        ecatalogEntity = (CreateEcatalogEntity) resultBundle.getSerializable("edit_ecatalog");
        customDialog = new CustomDialog(this, "Loading...");
        initView();
        initData();
    }

    private void initData() {
        mCreateCustomerName.setText(ecatalogEntity.getUserName());
        mCreateContacts.setText(ecatalogEntity.getContract());
        mCreateContactPhone.setText(ecatalogEntity.getPhone());
        dateTv.setText(ecatalogEntity.getDate());
        mCreateTitle.setText(ecatalogEntity.getTitle());
        mCreateTitle.setEnabled(false);
        mCreateTitle.setFocusable(false);
        mCreateTitle.setKeyListener(null);//重点
        mCreateCustomerName.setEnabled(false);
        mCreateCustomerName.setFocusable(false);
        mCreateCustomerName.setKeyListener(null);//重点
    }

    private void initView() {
        back = findViewById(R.id.ecatalog_create__back);
        back.setOnClickListener(this);
        titleTv = findViewById(R.id.ecatalog_create__toobar_title);
        titleTv.setText(getResources().getString(R.string.edit_ecatalog));
        mCreateTitle = (EditText) findViewById(R.id.ecatalog_create__title);
        mCreateCustomerName = (EditText) findViewById(R.id.ecatalog_create__customer_name);
        mCreateContacts = (EditText) findViewById(R.id.ecatalog_create__contacts);
        mCreateExprotContract = (TextView) findViewById(R.id.ecatalog_create_exprot_contract);
        mCreateExprotContract.setOnClickListener(this);
        mCreateContactPhone = (EditText) findViewById(R.id.ecatalog_create__contact_phone);
        mCreateUserImg = (ImageView) findViewById(R.id.ecatalog_create__user_img);
        mCreateUserName = (TextView) findViewById(R.id.ecatalog_create__user_name);
        mCreateUserPhone = (TextView) findViewById(R.id.ecatalog_create__user_phone);
        mCreateCreate = (TextView) findViewById(R.id.ecatalog_create__create);
        mCreateCreate.setOnClickListener(this);
        dateView = findViewById(R.id.ecatalog_create__date_view);
        dateView.setOnClickListener(this);
        dateTv = findViewById(R.id.ecatalog_create__date);
        mScopeTv = findViewById(R.id.ecatalog_create__scope);
        mScopeTv.setOnClickListener(this);
        scopeView = findViewById(R.id.ecatalog_create__scope_view);
        scopeView.setVisibility(View.GONE);
       // mCreateContactPhone.setOnTouchListener(this);
       // mCreateCustomerName.setOnTouchListener(this);
       // mCreateContacts.setOnTouchListener(this);
       // mCreateTitle.setOnTouchListener(this);
        UserResponse userResponse = Application.getInstance().getUserResponse();
        if (userResponse != null) {
            mCreateUserName.setText(userResponse.getName());
            mCreateUserPhone.setText(userResponse.getPhone());
            Glide.with(Application.getInstance().getCurrentActivity())
                    .load(userResponse.getAtatarUrl())
                    .error(me.nereo.multi_image_selector.R.drawable.default_error)
                    .centerCrop()
                    .into(mCreateUserImg);
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ecatalog_create__back:
                finish();
                break;
            case R.id.ecatalog_create__create:
                editEcatalog();
                break;
            case R.id.ecatalog_create__date_view:
                InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(back.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                showPopWindowDatePicker();
                break;
            case R.id.ecatalog_create__scope:
                InputMethodManager imm1 = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(back.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                showPopWindowScopePicker();
                break;
            case R.id.ecatalog_create_exprot_contract://导入联系人
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                break;
        }
    }

    //修改报价单
    private void editEcatalog() {
        customDialog.show();
        final CreateEcatalogRequest request = new CreateEcatalogRequest();
        request.setContacts(mCreateContacts.getText().toString());
        request.setName(mCreateTitle.getText().toString());
        request.setPhone(mCreateContactPhone.getText().toString());
        request.setUserName(mCreateCustomerName.getText().toString());
        request.setValidityDate(dateTv.getText().toString()+" 23:59:59");
        request.setId(ecatalogEntity.getEcatalogId());
        /*if ("大陆".equals(mScopeTv.getText().toString())) {
            request.setScope("1");//大陆1或者非大陆2
        } else
            request.setScope("2");//大陆1或者非大陆2
*/
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ECATALOG_EDIT, new OkHttpClientManager.ResultCallback<CreateEcatalogResponse>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(CreateEcatalogResponse response) {
                customDialog.dismiss();
                if (response.getEcatalogId() != null) {
                    Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.edit_success), Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new BackRefreshOneEvent("1"));
                    EcatalogDetailActivity.activity.finish();
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), EcatalogDetailActivity.class);
                    intent.putExtra("ecatalog_id", response.getEcatalogId() + "");
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, CreateEcatalogResponse.class);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
        switch (view.getId()) {
            case R.id.ecatalog_create__title:
                mCreateTitle.setFocusable(true);
                mCreateTitle.setFocusableInTouchMode(true);
                mCreateTitle.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(mCreateTitle.getWindowToken(), 0);
                break;
            case R.id.ecatalog_create__customer_name:
                mCreateCustomerName.setFocusable(true);
                mCreateCustomerName.setFocusableInTouchMode(true);
                mCreateCustomerName.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(mCreateCustomerName.getWindowToken(), 0);
                break;
            case R.id.ecatalog_create__contacts:
                mCreateContacts.setFocusable(true);
                mCreateContacts.setFocusableInTouchMode(true);
                mCreateContacts.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(mCreateContacts.getWindowToken(), 0);
                break;
            case R.id.ecatalog_create__contact_phone:
                mCreateContactPhone.setFocusable(true);
                mCreateContactPhone.setFocusableInTouchMode(true);
                mCreateContactPhone.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(mCreateContactPhone.getWindowToken(), 0);
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_CONTACT:
                mIntent = data;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    //申请授权，第一个参数为要申请用户授权的权限；第二个参数为requestCode 必须大于等于0，主要用于回调的时候检测，匹配特定的onRequestPermissionsResult。
                    //可以从方法名requestPermissions以及第二个参数看出，是支持一次性申请多个权限的，系统会通过对话框逐一询问用户是否授权。
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);

                } else {
                    //如果该版本低于6.0，或者该权限已被授予，它则可以继续读取联系人。
                    getContacts(data);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户成功授予权限
                getContacts(mIntent);
            } else {
                Toast.makeText(this, "你拒绝了此应用对读取联系人权限的申请！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getContacts(Intent data) {
        if (data == null) {
            return;
        }

        Uri contactData = data.getData();
        if (contactData == null) {
            return;
        }
        String name = "";
        String phoneNumber = "";

        Uri contactUri = data.getData();
        Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
        if (cursor.moveToFirst()) {
            name = cursor
                    .getString(cursor
                            .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = cursor
                    .getString(cursor
                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String id = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            if (hasPhone.equalsIgnoreCase("1")) {
                hasPhone = "true";
            } else {
                hasPhone = "false";
            }
            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = getContentResolver()
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                        + " = " + id, null, null);
                while (phones.moveToNext()) {
                    phoneNumber = phones
                            .getString(phones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }
            cursor.close();

            mCreateContacts.setText(name);
            mCreateContactPhone.setText(phoneNumber);
        }
    }

    //popwindow显示
    private void showPopWindowDatePicker() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_popup_datepicker, null);
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(findViewById(R.id.ecatalog_create__parent),
                Gravity.BOTTOM, 0, 0);
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        Calendar calendar = Calendar.getInstance();
        if (!"".equals(dateTv.getText().toString())) {
            String times = dateTv.getText().toString();
            String years = times.substring(0, 4);
            String month = times.substring(5, 7);
            String day = times.substring(8, 10);
            if ("0".equals(month.substring(0, 1))) {
                month = month.substring(1, 2);
            }
            if ("0".equals(day.substring(0, 1))) {
                day = day.substring(1, 2);
            }
            year = Integer.parseInt(years);
            monthOfYear = Integer.parseInt(month) - 1;
            dayOfMonth = Integer.parseInt(day);
        } else {
            year = calendar.get(Calendar.YEAR);
            monthOfYear = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        }
        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int years,
                                      int monthOfYears, int dayOfMonths) {
                year = years;
                monthOfYear = monthOfYears;
                dayOfMonth = dayOfMonths;
            }
        });
        TextView dateCancel = (TextView) view.findViewById(R.id.date_cancel);
        TextView datesubmit = (TextView) view.findViewById(R.id.date_submit);
        dateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        datesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = "";
                String day = "";
                String date = "";
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = dayOfMonth + "";
                }
                if (monthOfYear < 9) {
                    month = "0" + (monthOfYear + 1);
                } else {
                    month = (monthOfYear + 1) + "";
                }
                date = year + "-" + month + "-" + day;
                dateTv.setText(date);
                window.dismiss();
            }
        });
    }

    //popwindow显示
    private void showPopWindowScopePicker() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindows_scope, null);
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(findViewById(R.id.ecatalog_create__parent),
                Gravity.BOTTOM, 0, 0);
        TextView dateCancel = (TextView) view.findViewById(R.id.item_popupwindows_cancel);
        final TextView dalu = (TextView) view.findViewById(R.id.item_popupwindows_dalu);
        final TextView feidalu = (TextView) view.findViewById(R.id.item_popupwindows_feidalu);
        dateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        dalu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScopeTv.setText(getResources().getString(R.string.mainland1));
                window.dismiss();
            }
        });
        feidalu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScopeTv.setText(getResources().getString(R.string.no_mainland));
                window.dismiss();
            }
        });
    }
}

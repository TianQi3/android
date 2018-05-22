package com.humming.asc.sales.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;

public class TextEditorActivity extends AbstractActivity {
    public static final int ACTIVITY_TEXT_EDITOR_RESULT = 10001;
    public static final int ACTIVITY_DESCRIPTION_RESULT = 10002;
    public static final int ACTIVITY_EDIT_TASK_RESULT = 10003;
    public static final int ACTIVITY_EDIT_DESC_RESULT = 10004;
    public static final int ACTIVITY_EDIT_DAILY_CALL_CALLPLAN_RESULT = 10005;
    public static final int ACTIVITY_EDIT_DAILY_CALL_LOCATION_RESULT = 10006;
    public static final int ACTIVITY_EDIT_DAILY_CALL_NOTE_RESULT = 10007;
    public static final int ACTIVITY_EDIT_DAILY_CALL_RESULT_RESULT = 10008;
    public static final int ACTIVITY_EDIT_COMMENT_ADD_RESULT = 10009;

    //add leads
    public static final int ACTIVITY_ADD_LEADS_ENGLISH_NAME = 10101;
    public static final int ACTIVITY_ADD_LEADS_CHINESE_NAME = 10102;
    public static final int ACTIVITY_ADD_LEADS_ADDRESS = 10103;
    public static final int ACTIVITY_ADD_LEADS_FRISTNAME = 10104;
    public static final int ACTIVITY_ADD_LEADS_LASTNAME = 10105;
    public static final int ACTIVITY_ADD_LEADS_TITLE = 10106;
    public static final int ACTIVITY_ADD_LEADS_WORK_PHONE = 10107;
    public static final int ACTIVITY_ADD_LEADS_MOBILE_PHONE = 10108;
    public static final int ACTIVITY_ADD_LEADS_EMAIL = 10109;
    public static final int ACTIVITY_ADD_LEADS_DEVELOPE_PLAN = 10110;
    public static final int ACTIVITY_ADD_LEADS_CUSTOMER_DEMAND = 10111;
    public static final int ACTIVITY_ADD_LEADS_CUSTOMER_TAG1 = 10112;
    public static final int ACTIVITY_ADD_LEADS_CUSTOMER_TAG2 = 10113;
    public static final int ACTIVITY_ADD_LEADS_CUSTOMER_TAG3 = 10114;
    public static final int ACTIVITY_ADD_LEADS_CUSTOMER_TAG4 = 10115;
    public static final int ACTIVITY_ADD_LEADS_CUSTOMER_TAG5 = 10116;

    public static final String KEY_TEXT = "text";
    public static final String CURRENT_CLASS = "current_class";
    public static final String CURRENT_CLASS_TASK_TASK = "current_class_task_task";
    public static final String CURRENT_CLASS_TASK_DESC = "current_class_task_desc";
    public static final String CURRENT_CLASS_TASK_TASK_EDIT = "current_class_task_task_edit";
    public static final String CURRENT_CLASS_TASK_DESC_EDIT = "current_class_task_desc_edit";

    public static final String CURRENT_CLASS_NAME = "name";
    public static final String CURRENT_TEXT_VALUE = "text_value";
    private TextEditorData textEditorData;
    private ActionBar actionBar;
    private EditText editText;
    private boolean changed = true;
    private MenuItem menuItem;
    private String currentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        currentClass = intent.getStringExtra(CURRENT_CLASS);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        editText = (EditText) findViewById(R.id.text);
        String name = getIntent().getStringExtra(TextEditorActivity.CURRENT_TEXT_VALUE);
        if (!"".equals(name)) {
            editText.setText(name);
        }
        textEditorData = Application.getInstance().getTextEditorData();
        boolean singleLine = textEditorData.isSingleLine();

        actionBar.setTitle(textEditorData.getTitle());
        editText.setLines(textEditorData.getMaxLines());
       // editText.setMaxLines(textEditorData.getMaxLines());
        editText.setHint(textEditorData.getHint());
        editText.setInputType(textEditorData.getInputType());
        editText.setSingleLine(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_editor, menu);
        menuItem = menu.findItem(R.id.action_save);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem.setVisible(changed);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if (CURRENT_CLASS_TASK_TASK.equals(currentClass) || CURRENT_CLASS_TASK_TASK_EDIT.equals(currentClass)) {
                setTextResult(TextEditorActivity.ACTIVITY_TEXT_EDITOR_RESULT);
            }
            if (CURRENT_CLASS_TASK_DESC.equals(currentClass) || CURRENT_CLASS_TASK_DESC_EDIT.equals(currentClass)) {
                setTextResult(TextEditorActivity.ACTIVITY_TEXT_EDITOR_RESULT);
            } else {
                setTextResult(TextEditorActivity.ACTIVITY_TEXT_EDITOR_RESULT);
            }
            return true;
        }
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTextResult(int data) {
        Bundle resultBundle = new Bundle();
        resultBundle.putString(
                TextEditorActivity.KEY_TEXT,
                editText.getText().toString());
        Intent resultIntent = new Intent()
                .putExtras(resultBundle);
        setResult(
                data,
                resultIntent);
        finish();
    }
}

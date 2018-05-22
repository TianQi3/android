package com.humming.asc.sales.activity.Stocks;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.SimpleSelectorAdapter;

import java.util.List;

public class SimpleSelectorActivity extends AbstractActivity {
	public static final String SELECTED_ITEM_CODE = "selected_item_code";
	public static final String STRING_LIST = "string_array_list";
	public static final String SELECTOR_TITLE = "title";
	public static final String SELECTED_STRING = "";
	public static final String SELECTED_POSITION = "position";
	private ArrayAdapter<String> arrayAdapter;
	private int selectedItemCode;
	private ListView listView;
	private EditText etFinder;
	private TextView tfCancel;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_selector);
		listView = (ListView) findViewById(R.id.activity_simple_selector__list_view);
		etFinder = (EditText) findViewById(R.id.activity_simple_selector__finder);
		tfCancel = (TextView) findViewById(R.id.activity_simple_selector__finder_cancel);


		Bundle bound = getIntent().getExtras();
		title = bound.getString(SELECTOR_TITLE);
		selectedItemCode = bound.getInt(SELECTED_ITEM_CODE);
		List<String> list = bound
				.getStringArrayList(SimpleSelectorActivity.STRING_LIST);
		arrayAdapter = new SimpleSelectorAdapter(this, list);
		listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent resultIntent = new Intent();
				resultIntent.putExtra(SimpleSelectorActivity.SELECTED_STRING,arrayAdapter.getItem(position));
				if(title.equals(getResources().getString(R.string.city))){
					resultIntent.putExtra(SimpleSelectorActivity.SELECTED_POSITION,position+"");
				}
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
		tfCancel.setVisibility(View.VISIBLE);
		tfCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//etFinder.setText("");
				finish();
			}
		});
		etFinder.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString();
				/*if (text.length() > 0) {
					tfCancel.setVisibility(View.VISIBLE);
				}*/
				arrayAdapter.getFilter().filter(etFinder.getText());
			}
		});
		etFinder.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (EditorInfo.IME_ACTION_SEARCH == actionId) {
					query(etFinder.getText().toString());
				}
				return true;
			}
		});
	}

	private void query(String msg) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(SimpleSelectorActivity.SELECTED_STRING,msg);
		/*if(title.equals(getResources().getString(R.string.city))){
			resultIntent.putExtra(SimpleSelectorActivity.SELECTED_POSITION,position+"");
		}*/
		setResult(RESULT_OK, resultIntent);
		finish();
	}
}

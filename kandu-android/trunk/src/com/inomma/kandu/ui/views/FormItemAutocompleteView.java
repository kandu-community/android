package com.inomma.kandu.ui.views;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.inomma.kandu.model.FormItem;
import com.inomma.kandu.model.FormSubmission;
import com.inomma.kandu.model.FormSubmissionItem;
import com.inomma.kandu.model.UserForm;

public class FormItemAutocompleteView extends FormItemView {
	protected AutoCompleteTextView autoCompleteTextView;

	private UserForm userForm;
	private SubmissionsAutocompleteAdapter adapter;
	private Integer selectedId = -1;
	private ItemSelectedListener itemSelectedListener;
	private FormSubmission selectedItem;

	public static interface ItemSelectedListener {
		void onItemSelected(FormSubmission formSubmission);
	}

	public FormItemAutocompleteView(Context context) {
		super(context);
	}

	public FormItemAutocompleteView(Context context, FormItem item,
			UserForm userForm) {
		super(context, item);
		this.userForm = userForm;

		autoCompleteTextView
				.setAdapter(adapter = new SubmissionsAutocompleteAdapter(
						getContext(), android.R.layout.simple_list_item_1,
						this.userForm));

		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				selectedId = adapter.getItem(pos).getId();
				selectedItem = adapter.getItem(pos);
				if (itemSelectedListener != null) {
					itemSelectedListener.onItemSelected(adapter.getItem(pos));
				}
				showCheckMark();
			}
		});

	}

	public FormItemAutocompleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FormItemAutocompleteView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void fillContent(Context context) {
		super.fillContent(context);
		autoCompleteTextView = new AutoCompleteTextView(context);
		autoCompleteTextView.setEms(0);
		autoCompleteTextView.setThreshold(1);
		autoCompleteTextView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		if (item != null){
			autoCompleteTextView.setHint(item.getHint()!=null?item.getHint():item.getVisibleName());
		}
		
		addView(autoCompleteTextView);

	}

	@Override
	public void setValue(FormSubmissionItem value) {
		if (value == null) {
			autoCompleteTextView.setText("");
		} else {

			try {
				JSONObject jsonObject = new JSONObject(value.getValue());
				FormSubmission formSubmission = new FormSubmission(userForm,
						jsonObject);
				autoCompleteTextView.setText(formSubmission.toString());
				selectedId = formSubmission.getId();
				selectedItem = formSubmission;
				showCheckMark();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public Object getValue() {
		if (selectedItem == null) {
			return null;
		}

		return selectedItem.getSubmissionJson().toString();

	}

	@Override
	public String getValueString() {
		if (selectedId < 0) {
			return null;
		}
		return selectedId + "";
	}

	public ItemSelectedListener getItemSelectedListener() {
		return itemSelectedListener;
	}

	public void setItemSelectedListener(
			ItemSelectedListener itemSelectedListener) {
		this.itemSelectedListener = itemSelectedListener;
	}

}

package com.inomma.kandu.ui.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.inomma.kandu.model.FormItem;
import com.inomma.kandu.model.FormSubmission;
import com.inomma.kandu.model.FormSubmissionItem;
import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.ui.views.FormItemAutocompleteView.ItemSelectedListener;

public class FormItemManyToManyView extends FormItemView {

	FormItemAutocompleteView autoCompleteTextView;
	UserForm userForm;
	List<Integer> selectedValues = new ArrayList<Integer>();
	public FormItemManyToManyView(Context context) {
		super(context);
	}

	public FormItemManyToManyView(Context context, FormItem item,
			UserForm userForm) {
		super(context, item);
		this.userForm = userForm;
		addAutoCompleteView();

	}

	public FormItemManyToManyView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FormItemManyToManyView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	protected void fillContent(Context context) {
		super.fillContent(context);

	}

	private void addAutoCompleteView() {
		autoCompleteTextView = new FormItemAutocompleteView(getContext(), item,
				userForm);
		autoCompleteTextView.setItemSelectedListener(new ItemSelectedListener() {
			
			@Override
			public void onItemSelected(FormSubmission formSubmission) {
				selectedValues.add(formSubmission.getId());
				valueAdded(formSubmission.toString());
			}
		});
		autoCompleteTextView.setText("Please type to add");
		addView(autoCompleteTextView);
	}
	
	private void valueAdded(String text){
		showCheckMark();
		autoCompleteTextView.setValue(null);
		TextView textView = new TextView(getContext());
		textView.setText(text);
		addView(textView, getChildCount()-1);
	}

	@Override
	public void setValue(FormSubmissionItem value) {
		
		System.out.println(value);
	}

	@Override
	public Object getValue() {
		return selectedValues.size()>0?selectedValues.toString():null;
	}

	@Override
	public String getValueString() {
		return selectedValues.toString();
	}
}

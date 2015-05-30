package com.inomma.kandu.ui.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.inomma.kandu.Utils;
import com.inomma.kandu.model.FormItem;
import com.inomma.kandu.model.FormSubmissionItem;

public class FormItemSingleChoiceView extends FormItemChoiceView {

	private Spinner spinner;

	public FormItemSingleChoiceView(Context context) {
		super(context);
	}

	public FormItemSingleChoiceView(Context context, FormItem item) {
		super(context, item);
	}

	public FormItemSingleChoiceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FormItemSingleChoiceView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void fillContent(Context context) {
		super.fillContent(context);
		spinner = new Spinner(context);
		List<String> choices = new ArrayList<String>(Arrays.asList(item
				.getChoices()));
		choices.add(0, "None");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_dropdown_item, choices);
		spinner.setAdapter(dataAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if (pos != 0)
					onChoiceChanged(item.getChoices()[pos - 1]);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				onChoiceChanged();

			}
		});

		addView(spinner);
		if(showDefaultValue()){
			spinner.setSelection(1);
		}

	}

	@Override
	public void setValue(FormSubmissionItem value) {
		spinner.setSelection(getChoiceIntex(value.getValue()) + 1);
	}

	@Override
	public String getValue() {
		if (spinner.getSelectedItemPosition() != 0)
			return Utils.keyFromName(spinner.getSelectedItem().toString());
		else
			return null;
	}

	
	@Override
	public String getValueString() {
		return getValue();
	}
}

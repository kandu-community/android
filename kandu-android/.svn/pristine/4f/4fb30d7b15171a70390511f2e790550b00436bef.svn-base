package com.inomma.kandu.ui.views;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import com.inomma.kandu.Utils;
import com.inomma.kandu.model.FormItem;
import com.inomma.kandu.model.FormSubmissionItem;

public class FormItemDateView extends FormItemTextView {

	private Calendar selected;

	public FormItemDateView(Context context) {
		super(context);
	}

	public FormItemDateView(Context context, FormItem item) {
		super(context, item);
	}

	public FormItemDateView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FormItemDateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void fillContent(Context context) {
		super.fillContent(context);
		editText.setFocusable(false);
		selected = Calendar.getInstance();
		
		editText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePicker();
			}
		});
		if(showDefaultValue()){
			editText.setText(Utils.dateToString(selected.getTime()));
		}
	}

	private void showDatePicker() {
		DatePickerDialog dpd = new DatePickerDialog(getContext(),
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						selected.set(year, monthOfYear, dayOfMonth);
						editText.setText(Utils.dateToString(selected.getTime()));

					}
				}, selected.get(Calendar.YEAR), selected
						.get(Calendar.MONTH), selected
						.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}

	@Override
	public void setValue(FormSubmissionItem value) {
		super.setValue(value);
		if(value.getValue()!=null && !value.getValue().equals("") && !value.getValue().equals("null")){
			try{
			selected.setTime(Utils.stringToDate(value.getValue()));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	
}

package com.inomma.kandu.ui.views;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.inomma.kandu.model.FormItem;
import com.inomma.kandu.model.FormSubmissionItem;

public class FormItemTextView extends FormItemView {

	protected EditText editText;

	public FormItemTextView(Context context) {
		super(context);
	}

	public FormItemTextView(Context context, FormItem item) {
		super(context, item);
	}

	public FormItemTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FormItemTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void fillContent(Context context) {
		super.fillContent(context);
		editText = new EditText(context);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

		editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				item.getMaxLenght()) });
		editText.setHorizontallyScrolling(false);
		if (item != null)
			editText.setHint(item.getHint());
		addView(editText);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				 if(s.length()>0){
					 showCheckMark();
				 }
				 else{
					 hideCheckmark();	
				 }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		if(showDefaultValue()){
			this.editText.setText("a");
		}
		
	}

	@Override
		public void setValue(FormSubmissionItem value) {
		if (value!=null && value.getValue() != null && !value.getValue().equals("")
				&& !value.getValue().equals("null")) {
			editText.setText(value.getValue());
		}
	}

	@Override
	public String getValue() {
		String editTextValue = editText.getText().toString();
		return "".equals(editTextValue) ? null : editTextValue;
	}

	@Override
	public String getValueString() {
		return getValue();
	}
}

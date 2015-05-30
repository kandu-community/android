package com.inomma.kandu.ui.views;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import com.inomma.kandu.model.FormItem;

public class FormItemDecimalView extends FormItemTextView
{
	public FormItemDecimalView(Context context)
	{
		super(context);
	}
	
	public FormItemDecimalView(Context context, FormItem item)
	{
		super(context, item);
	}

	public FormItemDecimalView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public FormItemDecimalView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void fillContent(Context context)
	{
		super.fillContent(context);
		editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL );
		if(showDefaultValue()){
			this.editText.setText("1");
		}
		
	}
}
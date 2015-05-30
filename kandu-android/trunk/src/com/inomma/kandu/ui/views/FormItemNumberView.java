 package com.inomma.kandu.ui.views;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import com.inomma.kandu.model.FormItem;

public class FormItemNumberView extends FormItemTextView
{
	public FormItemNumberView(Context context)
	{
		super(context);
	}
	
	public FormItemNumberView(Context context, FormItem item)
	{
		super(context, item);
	}

	public FormItemNumberView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public FormItemNumberView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void fillContent(Context context)
	{
		super.fillContent(context);
		editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
	
		if(showDefaultValue()){
			this.editText.setText("1");
		}
		
	}
}

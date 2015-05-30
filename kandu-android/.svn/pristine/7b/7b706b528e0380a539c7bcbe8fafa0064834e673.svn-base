package com.inomma.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class ViewUtils {
	
	private static Context context;
	private static float density;
	
	public static void init(Context context)
	{
		if(context == null)
			Log.e("ViewUtils", "Init with null context.");
		if(ViewUtils.context != null)
		{
			Log.e("ViewUtils", "Init was already called.");
			return;
		}
		ViewUtils.context = context;
		density = context.getResources().getDisplayMetrics().density;
	}
	
	public static int pxToDp(int px)
	{
		return (int) ((px * density) + 0.49);
	}
	
	public static int dpToPx(int dp)
	{
		return (int) ((dp / density) + 0.49);
	}
	
	public static int getRadioGroupSelectedIndex(RadioGroup group)
	{
		if(group == null)
			return -1;
		int index = group.getCheckedRadioButtonId();
		return index == -1? -1 : group.indexOfChild(group.findViewById(index));
	}
	
	public static void removeFromParent(View v)
	{
		((ViewGroup) v.getParent()).removeView(v);
	}
	
	public static void checkRadioAt(RadioGroup group, int index)
	{
		if(index >= group.getChildCount() || index < 0)
			return;
		((RadioButton) group.getChildAt(index)).setChecked(true);
	}
}

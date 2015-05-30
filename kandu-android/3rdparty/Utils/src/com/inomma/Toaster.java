package com.inomma;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Toaster {
	
	private static Toast toast;
	private static Context context;
	
	public static void init(Context context)
	{
		if(context == null)
			Log.e("Toaster", "Init with null context.");
		Toaster.context = context;
	}
	
	public static void show(String text, int duration)
	{
		if(context == null)
		{
			Log.e("Toaster", "Must call init(context) at first.");
			return;
		}
		if(toast!= null)
			toast.cancel();
		
		toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public static void show(int resId, int duration)
	{
		show(context.getString(resId), duration);
	}
}

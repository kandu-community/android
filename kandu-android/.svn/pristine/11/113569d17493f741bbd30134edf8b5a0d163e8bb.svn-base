package com.inomma;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Helper for work with shared preferences of application. 2 preferences are
 * available:<br/>
 * 1. settings<br/>
 * 2. data<br/>
 * <br/>
 * 
 * Actually there is no difference, but it's easer to understand which of the
 * saved preferences are application settings and which are just data.
 */
public class SharedPreferencesHelper
{

	private static String separator = "#%&";
	private static SharedPreferences dataPreferences;
	private static SharedPreferences settingsPreferences;
	private static Context context;

	public static void init(Context context)
	{
		if (context == null)
			Log.e("SharedPreferencesHelper", "Init with null context.");
		if (SharedPreferencesHelper.context != null)
		{
			Log.e("SharedPreferencesHelper", "Init was already called.");
			return;
		}
		SharedPreferencesHelper.context = context;
		settingsPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		dataPreferences = context.getSharedPreferences("data", Activity.MODE_PRIVATE);
	}

	public static int getIntData(String key, int defaultValue)
	{
		return dataPreferences.getInt(key, defaultValue);
	}

	public static void putIntData(String key, int value)
	{
		dataPreferences.edit().putInt(key, value).commit();
	}

	public static float getFloatData(String key, float defaultValue)
	{
		return dataPreferences.getFloat(key, defaultValue);
	}

	public static void putFloatData(String key, float value)
	{
		dataPreferences.edit().putFloat(key, value).commit();
	}

	public static boolean getBooleanData(String key, boolean defaultValue)
	{
		return dataPreferences.getBoolean(key, defaultValue);
	}

	public static void putBooleanData(String key, boolean value)
	{
		dataPreferences.edit().putBoolean(key, value).commit();
	}

	public static long getLongData(String key, long defaultValue)
	{
		return dataPreferences.getLong(key, defaultValue);
	}

	public static void putLongData(String key, long value)
	{
		dataPreferences.edit().putLong(key, value).commit();
	}

	public static String getStringData(String key, String defaultValue)
	{
		return dataPreferences.getString(key, defaultValue);
	}

	public static void putStringData(String key, String value)
	{
		dataPreferences.edit().putString(key, value).commit();
	}

	public static String[] getStringArrayData(String key, String[] defaultValue)
	{
		String arrayStr = dataPreferences.getString(key, null);
		if (arrayStr == null)
			return defaultValue;

		return arrayStr.split(separator);
	}

	public static void putStringArrayData(String key, String[] array)
	{
		dataPreferences.edit().putString(key, join(array)).commit();
	}

	// //////////////////////////////////////////////////////////

	public static int getIntSetting(String key, int defaultValue)
	{
		return settingsPreferences.getInt(key, defaultValue);
	}

	public static void putIntSetting(String key, int value)
	{
		settingsPreferences.edit().putInt(key, value).commit();
	}

	public static float getFloatSetting(String key, float defaultValue)
	{
		return settingsPreferences.getFloat(key, defaultValue);
	}

	public static void putFloatSetting(String key, float value)
	{
		settingsPreferences.edit().putFloat(key, value).commit();
	}

	public static boolean getBooleanSetting(String key, boolean defaultValue)
	{
		return settingsPreferences.getBoolean(key, defaultValue);
	}

	public static void putBooleanSetting(String key, boolean value)
	{
		settingsPreferences.edit().putBoolean(key, value).commit();
	}

	public static long getLongSetting(String key, long defaultValue)
	{
		return settingsPreferences.getLong(key, defaultValue);
	}

	public static void putLongSetting(String key, long value)
	{
		settingsPreferences.edit().putLong(key, value).commit();
	}

	public static String getStringSetting(String key, String defaultValue)
	{
		return settingsPreferences.getString(key, defaultValue);
	}

	public static void putStringSetting(String key, String value)
	{
		settingsPreferences.edit().putString(key, value).commit();
	}

	public static String[] getStringArraySetting(String key, String[] defaultValue)
	{
		String arrayStr = settingsPreferences.getString(key, null);
		if (arrayStr == null)
			return defaultValue;

		return arrayStr.split(separator);
	}

	public static void putStringArraySetting(String key, String[] array)
	{
		settingsPreferences.edit().putString(key, join(array)).commit();
	}

	private static String join(String[] array)
	{
		if (array == null)
			return null;
		else if (array.length == 0)
			return "";
		else if (array.length == 1)
			return array[0];

		StringBuilder builder = new StringBuilder(array[0]);
		for (int i = 1; i < array.length; i++)
			builder.append(separator + array[i]);

		return builder.toString();
	}

	public static void removeSetting(String key)
	{
		settingsPreferences.edit().remove(key).commit();
	}

	public static void removeData(String key)
	{
		dataPreferences.edit().remove(key).commit();
	}
}

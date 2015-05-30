package com.inomma;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class NoExceptionJsonObject extends JSONObject {
	
	public static NoExceptionJsonObject fromJsonObject(JSONObject json) throws JSONException
	{
		return new NoExceptionJsonObject(json.toString());
	}
	
	public NoExceptionJsonObject(JSONObject source) throws JSONException
	{
		this(source.toString());
	}
	
	public NoExceptionJsonObject()
	{
		super();
	}
	
	public NoExceptionJsonObject(JSONTokener readFrom) throws JSONException
	{
		super(readFrom);
	}
	
	public NoExceptionJsonObject(Map copyFrom)
	{
		super(copyFrom);
	}
	
	public NoExceptionJsonObject(String json) throws JSONException
	{
		super(json);
	}
	
	public NoExceptionJsonObject(JSONObject copyFrom, String[] names) throws JSONException
	{
		super(copyFrom, names);
	}
	
	@Override
	public Object get(String name) {
		Object obj = null;
		try {
			obj = super.get(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	@Override
	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}
	
	public boolean getBoolean(String name, boolean defaultValue)
	{
		boolean result = defaultValue;
		try {
			result = super.getBoolean(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public double getDouble(String name){
		return getDouble(name, 0);
		
	}
	
	public double getDouble(String name, double defaultValue)
	{
		double result = defaultValue;
		try {
			result = super.getDouble(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public int getInt(String name) {
		return getInt(name, 0);
	}
	
	public int getInt(String name, int defaultValue) {
		int result = defaultValue;
		try {
			result = super.getInt(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public long getLong(String name) {
		return getLong(name, 0);
	}
	
	public long getLong(String name, long defaultValue) {
		long result = defaultValue;
		try {
			result = super.getLong(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String getString(String name) {
		return getString(name, null);
	}
	
	public String getString(String name, String defaultValue) {
		String result = defaultValue;
		try {
			result = super.getString(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}

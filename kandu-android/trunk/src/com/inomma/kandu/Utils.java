package com.inomma.kandu;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.inomma.kandu.model.FormListCategory;
import com.inomma.kandu.model.FormListItem;
import com.inomma.kandu.model.UserForm;
import com.koushikdutta.async.Util;

public class Utils {

	public static String[] stringArrayFromJson(JSONArray array)
			throws JSONException {
		String[] ret = new String[array.length()];
		for (int i = 0; i < array.length(); i++) {
			ret[i] = array.getString(i);
		}
		return ret;
	}
	
	public static Map<String, String> mapFromJsonArray(JSONArray array) throws JSONException {
		Map<String, String> map = new HashMap<String, String>();
		
		int len = array.length();
		
		for (int i = 0; i < len; i++) {
			String text = array.getString(i);
			String value = keyFromName(text);
			map.put(value, text);
		}
		
		return map;
	}

	public static Map<String, String> mapFromJsonObject(JSONObject object) throws JSONException {
		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> values = object.keys();
		
		while(values.hasNext()) {
			String value = (String) values.next();
			String text = object.getString(value);
			map.put(value, text);
		}
		
		return map;
	}

	public static int getRandomUniqueNumber(Context c) {

		SharedPreferences prefs = c.getSharedPreferences("settings", 0);
		Set<String> set = prefs.getStringSet("uniques", new HashSet<String>());

		String number = "1";

		while (set.contains(number)) {
			Random rand = new Random();
			number = (rand.nextInt(5000) + 1) + "";
		}
		set.add(number);
		prefs.edit().putStringSet("uniques", set).commit();
		return Integer.valueOf(number);

	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromFile(File file, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	}

	public static String stringFromList(List<String> array) {
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (String item : array) {
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append(item);
		}
		return sb.toString();
	}

	public static List<FormListCategory> formListsFromUserForms(
			List<UserForm> userForms) {
		LinkedHashMap<String, FormListCategory> categoriesMap = new LinkedHashMap<String, FormListCategory>();
		for (UserForm userForm : userForms) {
			FormListCategory formListCategory = categoriesMap.get(userForm
					.getForm().getCategory());
			if (formListCategory == null) {
				formListCategory = new FormListCategory();
				formListCategory.setName(userForm.getForm().getCategory());
				categoriesMap.put(userForm.getForm().getCategory(),
						formListCategory);
			}
			formListCategory.addFormListItem(new FormListItem(userForm, false));
			if (userForm.getForm().isEditable()) {
				formListCategory.addFormListItem(new FormListItem(userForm,
						true));
			}
		}

		return new ArrayList<FormListCategory>(categoriesMap.values());
	}

	public static String keyFromName(String name) {
		if (name == null)
			return null;
		return name.trim().replaceAll(" ", "_").replaceAll("[^_\\w\\d]", "").replaceAll("__", "_");//TODO

	}
	
	public static JSONObject mapToJsonObject(Map<String, Object> map) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		for(String key:map.keySet()){
			Object value = map.get(key);
			if(value ==null){
				value = "";
			}
			jsonObject.put(key, value.toString());
		}
		return jsonObject;
	}
	
	public static JSONArray removeFromJsonArray(JSONArray jsonArray,int position) throws JSONException{
		ArrayList<String> list = new ArrayList<String>();     
		int len = jsonArray.length();
		if (jsonArray != null) { 
		   for (int i=0;i<len;i++){ 
		    list.add(jsonArray.get(i).toString());
		   } 
		}
		//Remove the element from arraylist
		list.remove(position);
		//Recreate JSON Array
		JSONArray jsArray = new JSONArray();
		for(String item:list){
			jsArray.put(new JSONObject(item));
		}
		
		return jsArray;
	}
	
	public static String dateToString(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}
	
	public static Date stringToDate(String str){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return simpleDateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}
	
}

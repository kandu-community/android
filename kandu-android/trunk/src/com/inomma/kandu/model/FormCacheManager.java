package com.inomma.kandu.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Environment;

import com.inomma.SharedPreferencesHelper;
import com.inomma.kandu.R;
import com.inomma.kandu.Utils;

public class FormCacheManager {

	private static FormCacheManager instance = new FormCacheManager();

	public static FormCacheManager getInstance() {
		return instance;
	}

	private Random random;

	private FormCacheManager() {
		this.random = new Random(System.currentTimeMillis());
	}

	
	public void importCache(Activity a){
	    try {
	        Resources res = a.getResources();
	        InputStream in_s = res.openRawResource(R.raw.backup);

	        byte[] b = new byte[in_s.available()];
	        in_s.read(b);
	        String txt = new String(b);
	        
	        SharedPreferencesHelper.putStringData("cachedForms",
	        		txt);
	    } catch (Exception e) {
	        // e.printStackTrace();
	    }
	}
	public void importCache(Activity a,String path){
	    try {
	        InputStream in_s = new FileInputStream(new File(path));

	        byte[] b = new byte[in_s.available()];
	        in_s.read(b);
	        String txt = new String(b);
	        
	        SharedPreferencesHelper.putStringData("cachedForms",
	        		txt);
	        in_s.close();
	    } catch (Exception e) {
	        // e.printStackTrace();
	    }
	}
	public void addToCache(FormSubmission formSubmission) {
		try {
			JSONArray jsonArray = null;
			String savedFormsStr = SharedPreferencesHelper.getStringData(
					"cachedForms", null);
			if (savedFormsStr == null) {
				jsonArray = new JSONArray();
			} else {
				jsonArray = new JSONArray(savedFormsStr);
			}
			JSONObject formSubmissionJson = formSubmission.toJson();
			int uniqueID = random.nextInt();
			formSubmissionJson.put("uniqueKeyIdentifier", uniqueID);
			jsonArray.put(formSubmissionJson);
			SharedPreferencesHelper.putStringData("cachedForms",
					jsonArray.toString());
			formSubmission.setUniqueId(uniqueID);;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void replaceInCache(FormSubmission formSubmission) {
		try {
			JSONArray jsonArray = null;
			String savedFormsStr = SharedPreferencesHelper.getStringData(
					"cachedForms", null);
			if (savedFormsStr == null) {
				jsonArray = new JSONArray();
			} else {
				jsonArray = new JSONArray(savedFormsStr);
			}
			JSONObject formSubmissionJson = formSubmission.toJson();

			for(int i = 0; i<jsonArray.length();i++){
				if (formSubmission.getUniqueId() == jsonArray.getJSONObject(i)
						.getInt("uniqueKeyIdentifier")) {
					jsonArray.put(i, formSubmissionJson);
				}

			}
			SharedPreferencesHelper.putStringData("cachedForms",
					jsonArray.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public List<FormSubmission> getAllSavedForms() {
		List<FormSubmission> formSubmissions = new ArrayList<FormSubmission>();
		try {
			JSONArray jsonArray = null;
			String savedFormsStr = SharedPreferencesHelper.getStringData(
					"cachedForms", null);
			if (savedFormsStr == null) {
				jsonArray = new JSONArray();
			} else {
				jsonArray = new JSONArray(savedFormsStr);
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				FormSubmission formSubmission = new FormSubmission();
				formSubmission.fromJson(jsonArray.getJSONObject(i));
				formSubmissions.add(formSubmission);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return formSubmissions;
	}

	public void deleteFromCache(FormSubmission formSubmission) {
		try {
			JSONArray jsonArray = null;
			String savedFormsStr = SharedPreferencesHelper.getStringData(
					"cachedForms", null);
			if (savedFormsStr == null) {
				jsonArray = new JSONArray();
			} else {
				jsonArray = new JSONArray(savedFormsStr);
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				if (formSubmission.getUniqueId() == jsonArray.getJSONObject(i)
						.getInt("uniqueKeyIdentifier")) {
					jsonArray = Utils.removeFromJsonArray(jsonArray, i);
					break;
				}

			}
			SharedPreferencesHelper.putStringData("cachedForms",
					jsonArray.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

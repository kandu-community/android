package com.inomma.kandu.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class Response {

	protected JSONObject error;
	private String json;
	private boolean isOk;
	private String errorMessage;
	public static Activity a;

	public Response(String json) throws JSONException {
		this.json = json;
	}

	public JSONObject getJsonObject() {

		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public JSONArray getJsonArray(){
		try {
			return new JSONArray(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isOk() {
		return isOk;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}

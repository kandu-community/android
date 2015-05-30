package com.inomma.kandu.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

	private Map<String, Form> forms;
	
	public Config(JSONArray jsonArray) throws JSONException{
		this.forms = new HashMap<String,Form>(jsonArray.length());
		for(int i = 0; i<jsonArray.length();i++){
			JSONObject formObject = jsonArray.getJSONObject(i);
			Form form = new Form(formObject);
			form.setIndex(i);
			this.forms.put(form.getKey(), form);
		}
	}

	public Map<String, Form> getForms() {
		return forms;
	}
}

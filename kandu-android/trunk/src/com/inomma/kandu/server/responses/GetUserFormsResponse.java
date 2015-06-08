package com.inomma.kandu.server.responses;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.server.Response;
import com.nostra13.universalimageloader.utils.L;

public class GetUserFormsResponse extends Response{

	private List<UserForm> userForms ;
	public GetUserFormsResponse(String json) throws JSONException {
		super(json);
		JSONArray jsonArray = getJsonArray();
		userForms = new ArrayList<UserForm>(jsonArray.length());
		for(int i = 0; i<jsonArray.length();i++){
			UserForm userForm = new UserForm();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			userForm.setUrl(jsonObject.getString("url"));
			userForm.setVisibleName(jsonObject.getString("verbose_name"));
			userForm.setKey(jsonObject.getString("name"));
			userForms.add(userForm);
			L.d("form-url", userForm.getUrl());
		}
	}

	public List<UserForm> getUserForms() {
		return userForms;
	}

	
}

package com.inomma.kandu.server.responses;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inomma.kandu.model.FormSubmission;
import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.server.Response;

public class GetSubmissionsResponse extends Response {

	private JSONArray submissions;
	private List<FormSubmission> formSubmissions = new ArrayList<FormSubmission>();

	public GetSubmissionsResponse(String json, UserForm userForm) throws JSONException {
		super(json);
		JSONObject jsonObject = getJsonObject();
		setSubmissions(jsonObject.getJSONArray("results"));
		JSONArray array = getSubmissions(); 
		for (int i = 0; i < array.length(); i++) {
			FormSubmission formSubmission = new FormSubmission(userForm,array.getJSONObject(i));
			formSubmission.setForm(userForm);
			
			formSubmissions.add(formSubmission);
		}
	
	}
	
	public GetSubmissionsResponse(List<FormSubmission> formSubmissions) throws JSONException {
		super("[]");
		this.formSubmissions = formSubmissions;
	
	}

	public JSONArray getSubmissions() {
		return submissions;
	}
	public void setSubmissions(JSONArray submissions) {
		this.submissions = submissions;
	}
	
	public List<FormSubmission> getFormSubmissions(){
		return this.formSubmissions;
	}

}

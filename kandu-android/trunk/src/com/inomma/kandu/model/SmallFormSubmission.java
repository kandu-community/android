package com.inomma.kandu.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmallFormSubmission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2268190624875770598L;
	private Integer id;
	private Map<String, FormSubmissionItem> submissionItems = new HashMap<String, FormSubmissionItem>();
	private UserForm userForm;

	public SmallFormSubmission() {

	}

	public SmallFormSubmission(UserForm userForm) {
		this.userForm = userForm;
	}

	public SmallFormSubmission(JSONObject submissionJson, UserForm userForm)
			throws JSONException {
		this.userForm = userForm;

		for (FormItem formItem : this.userForm.getForm().getFormItems()) {
			String key = formItem.getKey();
			FormSubmissionItem formSubmissionItem = new FormSubmissionItem();
			if (formItem.getFormItemType() == FormItemType.IMAGE) {
				formSubmissionItem.setIsServerFile(true);
			}
			formSubmissionItem.setKey(key);
			if(!submissionJson.has(key)){
				continue;
			}
			formSubmissionItem.setValue(submissionJson.getString(key));
			this.submissionItems.put(formItem.getKey(), formSubmissionItem);
		}
		if (submissionJson.has("id")) {
			this.setId(submissionJson.getInt("id"));
		}
	}

	public FormSubmissionItem getFormSubmissionItem(String key) {
		return submissionItems.get(key);
	}

	public void putFormSubmissionItem(String key,
			FormSubmissionItem formSubmissionItem) {
		this.submissionItems.put(key, formSubmissionItem);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserForm getUserForm() {
		return this.userForm;
	}

	public Collection<FormSubmissionItem> getFormSubmissionItems() {
		return this.submissionItems.values();
	}

	public JSONObject toJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("form", userForm.getKey());
		JSONArray submissionItemsArray = new JSONArray();
		for (String key : submissionItems.keySet()) {
			submissionItemsArray.put(submissionItems.get(key).toJson());
		}
		jsonObject.put("items", submissionItemsArray);
		return jsonObject;
	}

	public SmallFormSubmission fromJson(JSONObject jsonObject)
			throws JSONException {
		if (jsonObject.has("id"))
			this.id = jsonObject.getInt("id");
		this.userForm = UserFormsHolder.getInstance().getUserFormWithKey(
				jsonObject.getString("form"));
		JSONArray submissionsArray = jsonObject.getJSONArray("items");
		for (int i = 0; i < submissionsArray.length(); i++) {
			FormSubmissionItem formSubmissionItem = new FormSubmissionItem();
			formSubmissionItem.fromJson(submissionsArray.getJSONObject(i));
			submissionItems
					.put(formSubmissionItem.getKey(), formSubmissionItem);
		}
		return this;
	}
}

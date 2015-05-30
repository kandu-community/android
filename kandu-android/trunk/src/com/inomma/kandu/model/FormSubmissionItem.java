package com.inomma.kandu.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class FormSubmissionItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2475325694370070687L;
	private String key;
	private String value;
	private boolean isLocalFile;
	private boolean isServerFile;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean getIsLocalFile() {
		return isLocalFile;
	}

	public void setIsLocalFile(boolean isLocalFile) {
		this.isLocalFile = isLocalFile;
	}

	public boolean isServerFile() {
		return isServerFile;
	}

	public void setIsServerFile(boolean isServerFile) {
		this.isServerFile = isServerFile;
	}

	public JSONObject toJson() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", key);
		jsonObject.put("value", value);
		jsonObject.put("isServerFile", isServerFile);
		jsonObject.put("isLocalFile", isLocalFile);
		return jsonObject;
	}
	
	public void fromJson(JSONObject jsonObject) throws JSONException{
		this.key = jsonObject.getString("key");
		this.key = this.key.replaceAll("__", "_");
		this.value = jsonObject.getString("value");
		this.value = this.value.replaceAll("__", "_");

		this.isServerFile = jsonObject.getBoolean("isServerFile");
		this.isLocalFile = jsonObject.getBoolean("isLocalFile");
		
	}
	
	public boolean isValueSubmitted(FormItem item){
		FormSubmission formSubmission;
		try {
			formSubmission = new FormSubmission(
					UserFormsHolder.getInstance().getUserFormWithKey(
							item.getLinkTo()), new JSONObject(
							getValue()));
			Integer id = formSubmission.getId();

			return id!=null;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}
}

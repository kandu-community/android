package com.inomma.kandu.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inomma.kandu.Utils;

public class FormItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8602462425924213507L;
	private String name;
	private String visibleName;
	private String key;
	private FormItemType formItemType;
	private boolean isRequired;
	private String hint = "";
	private String linkTo;
	private Map<String, String> choices;
	private int maxLenght = 100;
	private Map<String, String> visibleWhen;
	private boolean isLabelField = false;

	public FormItem(JSONObject jsonObject) throws JSONException {
		this.formItemType = FormItemType.getWithKey(jsonObject
				.getString("type"));
		this.name = jsonObject.getString("name");
		this.key = Utils.keyFromName(name);
		this.visibleName = name.substring(0, 1).toUpperCase()
				+ name.substring(1);
		if (jsonObject.has("choices")) {
			try {
				JSONArray choices = jsonObject.getJSONArray("choices");
				this.choices = Utils.mapFromJsonArray(choices);
			} catch(JSONException e) {
				JSONObject choices = jsonObject.getJSONObject("choices");
				this.choices = Utils.mapFromJsonObject(choices);
			}
		}
		if (jsonObject.has("required"))
			this.isRequired = jsonObject.getBoolean("required");
		if (jsonObject.has("hint"))
			this.setHint(jsonObject.getString("hint"));
		else
			this.setHint(this.name);
		if (jsonObject.has("to")) {
			this.linkTo = Utils.keyFromName(jsonObject.getString("to"));
		}
		if (jsonObject.has("max_length")) {
			this.maxLenght = jsonObject.getInt("max_length");
		}
		if (jsonObject.has("visible_when")) {
			visibleWhen = new HashMap<String, String>();
			JSONObject visibleWhenJson = jsonObject
					.getJSONObject("visible_when");
			Iterator<String> it = (Iterator<String>) visibleWhenJson.keys();
			while (it.hasNext()) {
				String key = it.next();
				String value = visibleWhenJson.getString(key);
				visibleWhen.put(key, value);
			}
		}
		if (jsonObject.has("label_field")) {
			this.setLabelField(jsonObject.getBoolean("label_field"));
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FormItemType getFormItemType() {
		return formItemType;
	}

	public void setFormItemType(FormItemType formItemType) {
		this.formItemType = formItemType;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getVisibleName() {
		return visibleName;
	}

	public void setVisibleName(String visibleName) {
		this.visibleName = visibleName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, String> getChoices() {
		return choices;
	}

	public void setChoices(Map<String, String> choices) {
		this.choices = choices;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getLinkTo() {
		return linkTo;
	}

	public void setLinkTo(String linkTo) {
		this.linkTo = linkTo;
	}

	public Map<String, String> getVisibleWhen() {
		return visibleWhen;
	}

	public int getMaxLenght() {
		return maxLenght;
	}

	public void setMaxLenght(int maxLenght) {
		this.maxLenght = maxLenght;
	}

	public boolean isLabelField() {
		return isLabelField;
	}

	public void setLabelField(boolean isLabelField) {
		this.isLabelField = isLabelField;
	}

	public String validate(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			String valueS = (String) value;
			switch (formItemType) {
			case DECIMAL:
				try {
					double valueD = Double.valueOf(valueS);
				} catch (Exception e) {
					return getVisibleName() + " should be number";
				}

				break;

			case NUMBER:
				try {
					long valueD = Long.valueOf(valueS);
				} catch (Exception e) {
					return getVisibleName() + " should be number";
				}

				break;
			case DATE:
				try {
					Date valueD = Utils.stringToDate(valueS);
				} catch (Exception e) {
					return getVisibleName() + " should be date";
				}

				break;
			default:
				break;
			}
		}
		return null;
	}
	

}

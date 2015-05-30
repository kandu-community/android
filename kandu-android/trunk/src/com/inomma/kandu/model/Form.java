package com.inomma.kandu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inomma.kandu.Utils;

public class Form implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7058307183013335552L;
	private List<FormItem> formItems;
	private String name;
	private String key;
	private String visibleName;
	private List<String> labelFields;
	private String category;
	private boolean showOnMap;
	private boolean isEditable = false;
	private int index;
	private List<Form> inlineForms = new ArrayList<Form>();
	private boolean cacheSubmissionsOffline;
	public Form(JSONObject jsonObject) throws JSONException {
		this.name = jsonObject.getString("name");
		this.key = Utils.keyFromName(name);
		this.visibleName = name.substring(0, 1).toUpperCase()
				+ name.substring(1);
		JSONArray formItems = jsonObject.getJSONArray("fields");
		this.formItems = new ArrayList<FormItem>(formItems.length());
		for (int i = 0; i < formItems.length(); i++) {
			this.formItems.add(new FormItem(formItems.getJSONObject(i)));
		}
		if (jsonObject.has("fields_for_label")) {
			this.labelFields = Arrays.asList(Utils
					.stringArrayFromJson(jsonObject
							.getJSONArray("fields_for_label")));
			int i = 0;
			for (String labelField : labelFields) {
				labelField = Utils.keyFromName(labelField);
				labelFields.set(i++, labelField);
			}
		} else {
			labelFields = new ArrayList<String>();
			int i = 0;
			for (FormItem formItem : this.formItems) {
				if (formItem.isLabelField()) {
					labelFields.add(formItem.getKey());
				}
			}
			if (labelFields == null || labelFields.size() == 0) {
				try{
				this.labelFields = Arrays.asList(new String[] { this.formItems
						.get(0).getKey() });
				}catch(Exception e){
					e.printStackTrace();
				}

			}
		}
		if (jsonObject.has("show_on_map")) {
			this.setShowOnMap(jsonObject.getBoolean("show_on_map"));
		}
		if (jsonObject.has("category")) {
			this.category = jsonObject.getString("category");
		}
		if (jsonObject.has("is_editable")) {
			this.isEditable = jsonObject.getBoolean("is_editable");
		}
		if (jsonObject.has("inlines")) {
			JSONArray inlines = jsonObject.getJSONArray("inlines");
			for (int i = 0; i < inlines.length(); i++) {
				inlineForms.add(new Form(inlines.getJSONObject(i)));
			}
		}
		if(jsonObject.has("cache_submissions_offline")){
			cacheSubmissionsOffline = jsonObject.getBoolean("cache_submissions_offline");
		}

	}

	public List<FormItem> getFormItems() {
		return formItems;
	}

	public void setFormItems(List<FormItem> formItems) {
		this.formItems = formItems;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVisibleName() {
		return visibleName;
	}

	public void setVisibleName(String visibleName) {
		this.visibleName = visibleName;
	}

	public List<String> getLabelFields() {
		return labelFields;
	}

	public void setLabelFields(List<String> labelFields) {
		this.labelFields = labelFields;
	}

	public boolean isShowOnMap() {
		return showOnMap;
	}

	public void setShowOnMap(boolean showOnMap) {
		this.showOnMap = showOnMap;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public List<Form> getInlineForms() {
		return this.inlineForms;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isCacheSubmissionsOffline() {
		return cacheSubmissionsOffline;
	}

	public void setCacheSubmissionsOffline(boolean cacheSubmissionsOffline) {
		this.cacheSubmissionsOffline = cacheSubmissionsOffline;
	}
}

package com.inomma.kandu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.inomma.kandu.ui.views.FormItemFileView;

public class FormSubmission implements Serializable {

	static final long serialVersionUID = 4971218580433025884L;
	private Integer uniqueId;
	private UserForm form;
	private boolean isSubmitted = true;
	private Integer dbId;
	private SmallFormSubmission smallFormSubmission;

	private String linkedTo;
	transient private JSONObject submissionJson;
	private List<SmallFormSubmission> inlineFormsSmallFormSubmissions = new ArrayList<SmallFormSubmission>();

	public FormSubmission(UserForm form) {
		this.form = form;
	}

	public FormSubmission(UserForm form, JSONObject submissionJson)
			throws JSONException {
		this.form = form;
		if (submissionJson.has("mainFormSubmission")) {

			fromJson(submissionJson);
			return;
			// submissionJson =
			// submissionJson.getJSONObject("mainFormSubmission");
		}

		this.setSmallFormSubmission(new SmallFormSubmission(submissionJson,
				this.form));
		this.setSubmissionJson(submissionJson);
		if (submissionJson.has("dbId")) {
			this.dbId = submissionJson.getInt("dbId");
		}
		for (UserForm inline : this.form.getInlineForms()) {
			String inlineSubmissionKey = inline.getKey().toLowerCase() + "_set";
			if (submissionJson.has(inlineSubmissionKey)) {
				JSONArray inlineFormSubmissions = submissionJson
						.getJSONArray(inlineSubmissionKey);
				for (int i = 0; i < inlineFormSubmissions.length(); i++) {
					JSONObject inlineSubmissionJson = inlineFormSubmissions
							.getJSONObject(i);
					SmallFormSubmission inlineFormSubmission = new SmallFormSubmission(
							inlineSubmissionJson, inline);
					inlineFormsSmallFormSubmissions.add(inlineFormSubmission);
				}
			}
			break;// TODO supports only 1 inline form
		}
	}

	public FormSubmission() {
		// TODO Auto-generated constructor stub
	}

	public UserForm getForm() {
		return form;
	}

	public void setForm(UserForm form) {
		this.form = form;
	}

	@Override
	public String toString() {
		String ret = "";
		if (form == null) {
			return super.toString();
		}
		int i = 0;

		for (String string : form.getForm().getLabelFields()) {
			FormItem formItem = form.getFormItemByKey(string);
			FormSubmissionItem item = smallFormSubmission
					.getFormSubmissionItem(string);
			if (item != null) {
				String value = smallFormSubmission
						.getFormSubmissionItem(string).getValue();

				String textToConcat = value;
				if (formItem.getFormItemType() == FormItemType.FOREIGN_KEY) {
					try {
						FormSubmission formSubmission = new FormSubmission(
								UserFormsHolder.getInstance()
										.getUserFormWithKey(
												formItem.getLinkTo()),
								new JSONObject(value));
						textToConcat = formSubmission.toString();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				ret += textToConcat
						+ (!textToConcat.endsWith(",")
								&& ++i < form.getForm().getLabelFields().size() ? ","
								: "");
			}
		}

		if (!isSubmitted) {
			ret = "NOT SUBMITTED: " + ret;
		}
		if (uniqueId != null) {
			ret = form.getVisibleName() + ": " + ret;
		}
		return ret;
	}

	public LatLng getCoordinates() {
		try {
			String coords = smallFormSubmission.getFormSubmissionItem(
					form.getGpsItem().getKey()).getValue();
			coords = coords.replaceAll("(\\[|\\])", "");
			Double lat = Double.valueOf(coords.split(",")[0]);
			Double lng = Double.valueOf(coords.split(",")[1]);
			return new LatLng(lat, lng);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer getId() {
		return smallFormSubmission.getId();
	}

	public boolean isSubmitted() {
		return isSubmitted;
	}

	public void setSubmitted(boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	public SmallFormSubmission getSmallFormSubmission() {
		return smallFormSubmission;
	}

	public void setSmallFormSubmission(SmallFormSubmission smallFormSubmission) {
		this.smallFormSubmission = smallFormSubmission;
	}

	public void addInlineSmallFormSubmission(
			SmallFormSubmission smallFormSubmission) {
		this.inlineFormsSmallFormSubmissions.add(smallFormSubmission);
	}

	public List<SmallFormSubmission> getInlineFormSubmissions() {
		return this.inlineFormsSmallFormSubmissions;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mainFormSubmission", smallFormSubmission.toJson());
		jsonObject.put("form", form.getKey());
		if (this.uniqueId != null) {
			jsonObject.put("uniqueKeyIdentifier", this.uniqueId);

		}
		if (this.linkedTo != null) {
			jsonObject.put("linkedTo", linkedTo);
		}
		if (this.dbId != null) {
			jsonObject.put("dbId", dbId);
		}
		JSONArray inlineArray = new JSONArray();
		for (SmallFormSubmission inlineFormSubmission : inlineFormsSmallFormSubmissions) {
			inlineArray.put(inlineFormSubmission.toJson());
		}
		jsonObject.put("inlineSubmissions", inlineArray);
		return jsonObject;
	}

	public void fromJson(JSONObject jsonObject) throws JSONException {
		this.smallFormSubmission = new SmallFormSubmission()
				.fromJson(jsonObject.getJSONObject("mainFormSubmission"));
		this.form = UserFormsHolder.getInstance().getUserFormWithKey(
				jsonObject.getString("form"));
		if (jsonObject.has("linkedTo")) {
			this.linkedTo = jsonObject.getString("linkedTo");
		}
		if (jsonObject.has("dbId")) {
			this.dbId = jsonObject.getInt("dbId");
		}
		JSONArray inlineArray = jsonObject.getJSONArray("inlineSubmissions");

		for (int i = 0; i < inlineArray.length(); i++) {
			SmallFormSubmission smallFormSubmission = new SmallFormSubmission();
			smallFormSubmission.fromJson(inlineArray.getJSONObject(i));
			inlineFormsSmallFormSubmissions.add(smallFormSubmission);
		}
		if (jsonObject.has("uniqueKeyIdentifier")) {
			this.uniqueId = jsonObject.getInt("uniqueKeyIdentifier");
		}
	}

	public Integer getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public JSONObject getSubmissionJson() {
		if (this.submissionJson == null) {
			try {
				this.submissionJson = toJson();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return submissionJson;
	}

	public void setSubmissionJson(JSONObject submissionJson) {
		this.submissionJson = submissionJson;
	}

	public Integer getDbId() {
		return dbId;
	}

	public void setDbId(Integer dbId) {
		this.dbId = dbId;
	}

	public String getLinkedTo() {
		return linkedTo;
	}

	public void setLinkedTo(String linkedTo) {
		this.linkedTo = linkedTo;
	}

	public boolean replaceAllSavedSubmissions(
			FormSubmission formSubmissionToReplace, int idToReplace) {
		boolean ret = false;
		for (FormItem formItem : form.getForm().getFormItems()) {
			if (formItem.getFormItemType() == FormItemType.FOREIGN_KEY
					&& formItem.getLinkTo().equals(
							formSubmissionToReplace.getForm().getKey())) {

				try {
					FormSubmissionItem currentItem = smallFormSubmission
							.getFormSubmissionItem(formItem.getKey());
					if(currentItem==null){
						continue;
					}
					FormSubmission currentSub = new FormSubmission(
							formSubmissionToReplace.getForm(), new JSONObject(
									currentItem.getValue()));
					if(currentSub==null){
						continue;
					}
					if ((int)currentSub.getUniqueId() != (int)formSubmissionToReplace
							.getUniqueId()) {
						continue;
					}
					formSubmissionToReplace.getSmallFormSubmission().setId(
							idToReplace);

					smallFormSubmission
							.getFormSubmissionItem(formItem.getKey())
							.setValue(
									formSubmissionToReplace.toJson().toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				ret = true;
			}
		}
		return ret;
	}

}

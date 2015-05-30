package com.inomma.kandu.server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.inomma.kandu.model.FormItem;
import com.inomma.kandu.model.FormItemType;
import com.inomma.kandu.model.FormSubmission;
import com.inomma.kandu.model.FormSubmissionItem;
import com.inomma.kandu.model.SmallFormSubmission;
import com.inomma.kandu.model.UserFormsHolder;
import com.inomma.kandu.server.request.SubmitFormRequest;
import com.inomma.kandu.server.responses.SubmitFormResponse;

public class FormSubmitter {

	private FormSubmission formSubmission;
	private Context context;
	private FormSubmissionListener listener;

	public static interface FormSubmissionListener {
		void formSubmitted(String errorMessage,FormSubmission formSubmission, Integer id);
	}

	public FormSubmitter(FormSubmission formSubmission, Context context,
			FormSubmissionListener listener) {
		super();
		this.formSubmission = formSubmission;
		this.context = context;
		this.listener = listener;
	}

	public void submitForm() {
		new SubmitFormRequest(context, formSubmission.getForm().getUrl(),
				getParams(formSubmission.getSmallFormSubmission()),
				new ResponseHandler<SubmitFormResponse>() {

					@Override
					public void handleResponse(SubmitFormResponse response) {
						if(response==null){
							listener.formSubmitted("Server error",formSubmission, null);
							return;
						}
						if (response.id == null) {
							listener.formSubmitted("Server error",formSubmission, null);
							return;
						}
						if (formSubmission.getInlineFormSubmissions().size() > 0) {
							submitInlines(response.id);

						} else {
							listener.formSubmitted(null,formSubmission, response.id);
						}

					}

				}).execute();
	}

	private void submitInlines(Integer id) {
		for (SmallFormSubmission inlineFormSubmission : formSubmission
				.getInlineFormSubmissions()) {
			FormSubmissionItem formSubmissionItem = new FormSubmissionItem();
			formSubmissionItem.setKey(formSubmission.getForm().getKey());
			formSubmissionItem.setValue(id + "");
			inlineFormSubmission.putFormSubmissionItem(
					formSubmissionItem.getKey(), formSubmissionItem);
			new SubmitFormRequest(context, inlineFormSubmission.getUserForm()
					.getUrl(), getParams(inlineFormSubmission),
					new ResponseHandler<SubmitFormResponse>() {

						@Override
						public void handleResponse(SubmitFormResponse response) {
							if (response.id != null) {
								listener.formSubmitted(null,formSubmission, response.id);
							} else {
								listener.formSubmitted("Server error",formSubmission, null);
							}
						}

					}).execute(context, "Submitting");
		}
	}
boolean isRequesting;
Integer submittedId;
	public  Map<String, Object> getParams(
			SmallFormSubmission smallFormSubmission) {
		Map<String, Object> params = new HashMap<String, Object>();

		for (FormSubmissionItem formSubmissionItem : smallFormSubmission
				.getFormSubmissionItems()) {
			FormItem formItem = smallFormSubmission.getUserForm()
					.getFormItemByKey(formSubmissionItem.getKey());
			if (formItem.getFormItemType() == FormItemType.FOREIGN_KEY) {
				try {

					final FormSubmission formSubmission = new FormSubmission(
							UserFormsHolder.getInstance().getUserFormWithKey(
									formItem.getLinkTo()), new JSONObject(
									formSubmissionItem.getValue()));
					Integer id = formSubmission.getId();
					if (id != null) {
						params.put(formSubmissionItem.getKey(), id);
//					} else {
//						isRequesting = true;
//						//new Thread(new Runnable() {
//							
//							//@Override
//						//	public void run() {
//								// TODO Auto-generated method stub
//								new FormSubmitter(formSubmission, context,
//										new FormSubmissionListener() {
//
//											@Override
//											public void formSubmitted(
//													String errorMessage, Integer id) {
//												submittedId = id;
//												isRequesting = false;
//											}
//										}).submitForm();;
//						//	}
//					//	}).start();
//					
//						
//						while (isRequesting) {
//							try {
//								Thread.sleep(10);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						}
					//	params.put(formSubmissionItem.getKey(), submittedId);

					}

				} catch (JSONException e) {
					try {
						params.put(formSubmissionItem.getKey(),
								formSubmissionItem.getValue());

					} catch (Exception ex) {
						ex.printStackTrace();

					}
					e.printStackTrace();

				}
			} else if (formSubmissionItem.getIsLocalFile()) {
				params.put(formSubmissionItem.getKey(), new File(
						formSubmissionItem.getValue()));
			} else {
				params.put(formSubmissionItem.getKey(),
						formSubmissionItem.getValue());
			}
		}
		if (smallFormSubmission.getId() != null) {
			params.put("id", smallFormSubmission.getId());
		}

		return params;
	}
}

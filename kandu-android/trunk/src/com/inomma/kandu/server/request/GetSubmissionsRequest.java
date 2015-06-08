package com.inomma.kandu.server.request;

import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;

import com.inomma.kandu.model.FormSubmission;
import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.server.Request;
import com.inomma.kandu.server.ResponseHandler;
import com.inomma.kandu.server.responses.GetSubmissionsResponse;
import com.inomma.kandu.sqlite.SubmissionsDataSource;
import com.inomma.utils.NetworkUtils;

public class GetSubmissionsRequest extends Request<GetSubmissionsResponse> {

	private String formUrl;
	private String searchText;
	private UserForm userForm;
	private Integer radius;
	private double latitude;
	private double longitude;

	public GetSubmissionsRequest(Context context, UserForm userForm,
			String formUrl, ResponseHandler<GetSubmissionsResponse> handler) {
		super(handler, context);
		this.formUrl = formUrl;
		this.userForm = userForm;
	}

	public GetSubmissionsRequest(Context context, UserForm userForm,
			String formUrl, int radius, double latitude, double longitude,
			ResponseHandler<GetSubmissionsResponse> handler) {
		super(handler, context);
		this.formUrl = formUrl;
		this.userForm = userForm;
		this.radius = radius;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public GetSubmissionsRequest(Context context, UserForm userForm,
			String formUrl, String searchText,
			ResponseHandler<GetSubmissionsResponse> handler) {
		super(handler, context);
		this.searchText = searchText;
		this.formUrl = formUrl;
		this.userForm = userForm;

	}

	@Override
	protected GetSubmissionsResponse handleResponse(String response)
			throws JSONException {
		return new GetSubmissionsResponse(response, userForm);
	}

	@Override
	public String getAction() {
		if (searchText != null) {
			return formUrl + "/search?query=" + searchText + "&page_size=1000";
		} else if (radius != null) {
			return String.format(formUrl
					+ "inRadius?lat=%.8f&long=%.8f&radius=%d", latitude,
					longitude, radius);
		} else {
			return formUrl + "?page_size=10000";
		}
	}

	@Override
	public void execute() {
		if (NetworkUtils.isNetworkAvailable()
				|| !this.userForm.getForm().isCacheSubmissionsOffline()) {
			super.execute();

		} else {

			
	
			
				((Activity) getContext()).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						SubmissionsDataSource dataSource = new SubmissionsDataSource(
								getContext());
						dataSource.open();
						final List<FormSubmission> formSubmissions = dataSource
								.getAllSubmissions(GetSubmissionsRequest.this.userForm.getForm(), searchText);
						try {
							GetSubmissionsRequest.this.handler.handleResponse(new GetSubmissionsResponse(
									formSubmissions));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dataSource.close();


					}
				});
			

		}
	}

}
	
package com.inomma.kandu.server.responses;

import org.json.JSONException;
import org.json.JSONObject;

import com.inomma.kandu.server.Response;

public class GetTokenResponse extends Response {

	private String token;
	private boolean isSuccess = false;
	public GetTokenResponse(String json) throws JSONException {
		super(json);
		try {
			this.setToken(getJsonObject().getString("token"));
			setSuccess(true);
		} catch (Exception e) {
			setSuccess(false);
		}
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}

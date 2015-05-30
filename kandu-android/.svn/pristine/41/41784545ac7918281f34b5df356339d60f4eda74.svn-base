package com.inomma.kandu.server.request;

import org.json.JSONException;

import android.content.Context;

import com.inomma.kandu.server.Request;
import com.inomma.kandu.server.RequestMethod;
import com.inomma.kandu.server.ResponseHandler;
import com.inomma.kandu.server.responses.GetTokenResponse;

public class GetTokenRequest extends Request<GetTokenResponse> {

	public GetTokenRequest(Context context,String username, String password,
			ResponseHandler<GetTokenResponse> handler) {
		super(handler,context);
		putParam("username", username);
		putParam("password", password);

	}

	@Override
	protected GetTokenResponse handleResponse(String response)
			throws JSONException {	
		return new GetTokenResponse(response);
	}

	@Override
	public String getAction() {
		return "/api/get-token/";
	}

	@Override
	public RequestMethod getMethod() {
		return RequestMethod.POST;
	}

}

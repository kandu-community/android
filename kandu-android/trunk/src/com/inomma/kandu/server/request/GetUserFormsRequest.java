package com.inomma.kandu.server.request;

import org.json.JSONException;

import android.content.Context;

import com.inomma.kandu.server.Request;
import com.inomma.kandu.server.ResponseHandler;
import com.inomma.kandu.server.responses.GetUserFormsResponse;

public class GetUserFormsRequest extends Request<GetUserFormsResponse> {

	public GetUserFormsRequest(Context context,ResponseHandler<GetUserFormsResponse> handler) {
		super(handler,context);
	}

	@Override
	protected GetUserFormsResponse handleResponse(String response)
				throws JSONException {
		saveCachedData(response);
		return new GetUserFormsResponse(response);
	}

	@Override
	public String getAction() {
		return "/api/getForms/";
	}

}

package com.inomma.kandu.server.request;

import org.json.JSONException;

import android.content.Context;

import com.inomma.kandu.server.Request;
import com.inomma.kandu.server.ResponseHandler;
import com.inomma.kandu.server.responses.GetIconsResponse;

public class GetIconsRequest extends Request<GetIconsResponse>{

	public GetIconsRequest(Context context,ResponseHandler<GetIconsResponse> handler) {
		super(handler,context);
	}

	@Override
	protected GetIconsResponse handleResponse(String response) throws JSONException {
		return new GetIconsResponse(response);
	}

	@Override
	public String getAction() {
		return "/api/getIcons/";
	}

}

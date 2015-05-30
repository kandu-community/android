package com.inomma.kandu.server.request;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.inomma.kandu.server.Request;
import com.inomma.kandu.server.ResponseHandler;
import com.inomma.kandu.server.responses.GetConfigResponse;

public class GetConfigRequest extends Request<GetConfigResponse>{

	public GetConfigRequest(Context context,ResponseHandler<GetConfigResponse> handler) {
		super(handler,context);
	}

	@Override
	protected GetConfigResponse handleResponse(String response)
			throws JSONException {
		Log.e("GetConfigRequest", response.toString());
		saveCachedData(response);
		return new GetConfigResponse(response);
	}

	@Override
	public String getAction() {
		return "/api/getConfig/";
	}
	
	@Override
	public String getCachedData() {
		// TODO Auto-generated method stub
		return super.getCachedData();
	}
	

}

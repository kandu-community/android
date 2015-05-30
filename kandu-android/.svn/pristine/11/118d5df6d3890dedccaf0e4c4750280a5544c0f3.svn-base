package com.inomma.kandu.server.request;

import java.util.Map;

import org.json.JSONException;

import android.content.Context;

import com.inomma.kandu.server.Request;
import com.inomma.kandu.server.RequestMethod;
import com.inomma.kandu.server.Response;
import com.inomma.kandu.server.ResponseHandler;
import com.inomma.kandu.server.responses.SubmitFormResponse;

public class SubmitFormRequest extends Request<SubmitFormResponse>{
	private String formUrl;
	public SubmitFormRequest(Context context,String formUrl,Map<String,Object> params,ResponseHandler<SubmitFormResponse> handler) {
		super(handler,context);
		if(params != null)
			for(String key:params.keySet())
				putParam(key, params.get(key));
		this.formUrl = formUrl;
	}

	@Override
	protected SubmitFormResponse handleResponse(String response)
			throws JSONException {
		return new SubmitFormResponse(response);
	}

	@Override
	public String getAction() {
		if(getParams().containsKey("id")){
			return formUrl+""+getParams().get("id").toString()+"/";
		}
		return formUrl;
	}
	
	@Override
	public RequestMethod getMethod() {
		if(getParams().containsKey("id")){
			return RequestMethod.PUT;
		}
		return RequestMethod.POST;
	}
}

package com.inomma.kandu.server.responses;

import org.json.JSONException;
import org.json.JSONObject;

import com.inomma.kandu.server.Response;

public class SubmitFormResponse extends Response {

	public Integer id;

	public SubmitFormResponse(String json) throws JSONException {
		super(json);
		try {
			JSONObject jsonObject = getJsonObject();
			id = jsonObject.getInt("id");
		} catch (Exception e) {

		}

	}

}

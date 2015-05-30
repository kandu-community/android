package com.inomma.kandu.server.responses;

import org.json.JSONException;
import org.json.JSONObject;

import com.inomma.kandu.model.Config;
import com.inomma.kandu.server.Response;

public class GetConfigResponse extends Response {

	private Config config;

	public GetConfigResponse(String json) throws JSONException {
		super(json);
		this.setConfig(new Config(getJsonArray()));

	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}

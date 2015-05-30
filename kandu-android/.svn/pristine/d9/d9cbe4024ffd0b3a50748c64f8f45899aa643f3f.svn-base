package com.inomma.kandu.server.responses;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inomma.kandu.server.MainSender;
import com.inomma.kandu.server.Response;

public class GetIconsResponse extends Response {
	private String organizationIconUrl;
	private List<String> parnerIconUrls = new ArrayList<String>();

	public GetIconsResponse(String json) throws JSONException {
		super(json);
		try {
			JSONObject iconsObj = getJsonObject();
			JSONArray icons = iconsObj.getJSONArray("results");
			for (int i = 0; i < icons.length(); i++) {
				JSONObject iconJsonObject = icons.getJSONObject(i);
				String iconFile = MainSender.BASE_URL
						+ iconJsonObject.getString("icon_file_url");
				if (!iconJsonObject.getBoolean("partner_icon")) {
					parnerIconUrls.add(iconFile);
				} else {
					organizationIconUrl = iconFile;
					parnerIconUrls.add(iconFile);//TODO:
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getOrganizationIconUrl() {
		return organizationIconUrl;
	}

	public List<String> getParnerIconUrls() {
		return parnerIconUrls;
	}

}

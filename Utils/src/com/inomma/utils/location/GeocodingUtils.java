package com.inomma.utils.location;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.inomma.Toaster;
import com.inomma.utils.HttpHelper;
import com.inomma.utils.HttpHelper.JsonObjectResponseHandler;
import com.inomma.utils.NetworkUtils;

public class GeocodingUtils {
	
	public static void getAddressForLocationAsync(final LatLng location, final OnAddressLoadedListener listener) {
		if (!NetworkUtils.isNetworkAvailable()) {
			Toaster.show("No internet connection", Toast.LENGTH_SHORT);
			return;
		}
		if (listener == null)
			return;
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=false";
		new HttpHelper(String.format(url, location.latitude, location.longitude)).httpGetObjectAsync(new JsonObjectResponseHandler() {

			@Override
			public void onSuccess(JSONObject result) {
				String text = location.latitude + ", " + location.longitude;
				if (result != null) {
					try {
						if (result.getString("status").equalsIgnoreCase("OK"))
							text = result.getJSONArray("results").getJSONObject(0).getString("formatted_address");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				listener.onLoad(text);
			}
		});
	}

	public static String getAddressForLocation(LatLng location) {
		if (!NetworkUtils.isNetworkAvailable()) {
			Toaster.show("No internet connection", Toast.LENGTH_SHORT);
			return null;
		}
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=false";
		JSONObject result = new HttpHelper(String.format(url, location.latitude, location.longitude)).httpGetObject();
		String text = location.latitude + ", " + location.longitude;
		if (result != null) {
			try {
				if (result.getString("status").equalsIgnoreCase("OK"))
					text = result.getJSONArray("results").getJSONObject(0).getString("formatted_address");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return text;
	}

	public interface OnAddressLoadedListener {
		public void onLoad(String result);
	}

}

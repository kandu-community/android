package com.inomma.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtils {
	
	private static Context context;
	
	public static void init(Context context)
	{
		if(context == null)
			Log.e("NetworkUtils", "Init with null context.");
		if(NetworkUtils.context != null)
		{
			Log.e("NetworkUtils", "Init was already called.");
			return;
		}
		NetworkUtils.context = context;
	}

	public static boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static boolean isLocationEnabled() {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

}

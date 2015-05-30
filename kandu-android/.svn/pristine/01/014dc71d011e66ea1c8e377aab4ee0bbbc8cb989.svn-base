package com.inomma.kandu;

import java.util.List;

import android.app.Application;
import android.graphics.Bitmap;

import com.inomma.SharedPreferencesHelper;
import com.inomma.Toaster;
import com.inomma.utils.NetworkUtils;
import com.inomma.utils.location.AdvancedLocationManager;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class KanduApplication extends Application {

	public String organizationIconUrl;
	public List<String> parnerIconUrls;
public static String FLURRY_API_KEY = "24JM9BVWK6TVWYRVQ5C6";
	@Override
	public void onCreate() {
		super.onCreate();

		Toaster.init(this);
		AdvancedLocationManager.init(this);
		NetworkUtils.init(this);
		SharedPreferencesHelper.init(this);

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)

				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		// Create global configuration and initialize ImageLoader with this
		// configuration
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPoolSize(1)
				.memoryCache(new WeakMemoryCache())
				.defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(config);
	}
}

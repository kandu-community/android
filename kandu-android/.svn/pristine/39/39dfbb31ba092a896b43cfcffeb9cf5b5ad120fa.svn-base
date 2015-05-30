package com.inomma.utils.location;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class AdvancedLocationManager
{

	private LocationManager manager;
	private LocationListener listener;
	private volatile List<LocationCallback> locationChangedCallbacks;
	private volatile List<LocationInterval> locationIntervals;
	private volatile List<ProviderStatusChangedCallback> providerStatusChangedCallbacks;
	private Location currentBestLocation;

	private int accuranceTimeout;
	private int minTime;
	private int minDistance;

	private boolean isWorking;
	private Thread intervalsThread;

	private static AdvancedLocationManager instance;

	private AdvancedLocationManager(Context context)
	{
		manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		minTime = 0;
		minDistance = 0;
		accuranceTimeout = 2 * 60 * 1000;
		locationChangedCallbacks = new ArrayList<LocationCallback>();
		providerStatusChangedCallbacks = new ArrayList<ProviderStatusChangedCallback>();
		listener = new LocationListener()
		{

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras)
			{
			}

			@Override
			public void onProviderEnabled(String provider)
			{
			}

			@Override
			public void onProviderDisabled(String provider)
			{
			}

			@Override
			public void onLocationChanged(Location location)
			{
				if (isBetterLocation(location))
				{
					currentBestLocation = location;
					for (LocationCallback callback : locationChangedCallbacks)
						callback.exec(location);
				}
			}
		};

		locationIntervals = new ArrayList<LocationInterval>();
	}

	/**
	 * This method should be called before using the singleton instance. An
	 * error is logged if this method was called more than 1 times.
	 * 
	 * @param context
	 */
	public static void init(Context context)
	{
		if (instance != null)
		{
			Log.e("AdvancedLocationManager", "init() was already called");
			return;
		}
		instance = new AdvancedLocationManager(context);
	}

	/**
	 * An error is logged if init() was not called.
	 * 
	 * @return the singleton instance.
	 */
	public static AdvancedLocationManager getInstance()
	{
		if (instance == null)
		{
			Log.e("AdvancedLocationManager", "init() must be called before getting instance");
		}
		return instance;
	}

	private boolean isBetterLocation(Location location)
	{
		if (currentBestLocation == null)
		{
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > accuranceTimeout;
		boolean isSignificantlyOlder = timeDelta < -accuranceTimeout;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer)
		{
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder)
		{
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate)
		{
			return true;
		} else if (isNewer && !isLessAccurate)
		{
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider)
		{
			return true;
		}
		return false;
	}

	private boolean isSameProvider(String provider1, String provider2)
	{
		if (provider1 == null)
		{
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	/**
	 * Adds callback which will be called at specified intervals The returned
	 * index can be used in future for removing added callback
	 * 
	 * @param callback
	 * @param milliseconds
	 *            this parameter will be rounded to nearest hundredfold
	 * @return the index of added callback in callbacks list.
	 */
	public int addLocationInterval(LocationCallback callback, long milliseconds)
	{
		if (callback == null)
			return -1;

		long ticks = (milliseconds + 50) / 100;

		LocationInterval interval = new LocationInterval(callback, ticks);

		// find first empty index
		int index = locationChangedCallbacks.indexOf(null);

		if (index >= 0)
		{
			// set new callback to that index if found
			locationIntervals.set(index, interval);
		} else
		{
			// just add if no null index was found
			locationIntervals.add(interval);
			index = locationIntervals.size() - 1;
		}

		// start interval thread if is not alive
		if (intervalsThread == null || !intervalsThread.isAlive())
		{
			intervalsThread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						while (!locationIntervals.isEmpty())
						{
							for (LocationInterval interval : locationIntervals)
							{
								if (interval != null)
									interval.tick(getCurrentBestLocation());
							}
							Thread.sleep(100);
						}
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			});

			intervalsThread.start();
		}

		return index;
	}

	/**
	 * Removes location interval for given id and all null intervals from list's
	 * tail.
	 * 
	 * @param id
	 * */
	public void removeLocationInterval(int id)
	{
		if (id < 0 || id >= locationIntervals.size())
			return;
		// set value of the index to null
		locationIntervals.set(id, null);

		// remove all null indexes from the tail of array
		while (!locationIntervals.isEmpty() && locationIntervals.get(locationIntervals.size() - 1) == null)
			locationIntervals.remove(locationIntervals.size() - 1);

		if (locationIntervals.isEmpty())
			intervalsThread = null;
	}

	/**
	 * Executes callback of interval with specified id immediately.
	 * 
	 * @param id
	 * */
	public void forceExecLocationInterval(int id)
	{
		if (id < 0 || id >= locationIntervals.size() || locationIntervals.get(id) == null)
			return;
		locationIntervals.get(id).callback.exec(currentBestLocation);
	}

	/**
	 * The returned index can be used in future for removing added callback
	 * 
	 * @param callback
	 * @return the index of added callback in callbacks list.
	 */
	public int addLocationChangedCallback(LocationCallback callback)
	{
		// find first empty index
		int index = locationChangedCallbacks.indexOf(null);

		if (index >= 0)
		{
			// set new callback to that index if found
			locationChangedCallbacks.set(index, callback);
			return index;
		} else
		{
			// just add if no null index was found
			locationChangedCallbacks.add(callback);
			return locationChangedCallbacks.size() - 1;
		}
	}

	/**
	 * Removes location change callback for given id. Actually item for id is
	 * just set to null so the other items ids are not changed.
	 * 
	 * @param id
	 */
	public void removeLocationChangedCallback(int id)
	{
		if (id >= 0 && id < locationChangedCallbacks.size())
			locationChangedCallbacks.set(id, null);
	}

	/**
	 * The returned index can be used in future for removing added update
	 * 
	 * @param callback
	 * @return the index of added callback in callbacks list.
	 */
	public int addProviderStatusChangedCallback(ProviderStatusChangedCallback callback)
	{
		try
		{
			int index = providerStatusChangedCallbacks.indexOf(null);
			providerStatusChangedCallbacks.set(index, callback);
			return index;
		} catch (NullPointerException e)
		{
			providerStatusChangedCallbacks.add(callback);
			return providerStatusChangedCallbacks.size() - 1;
		}
	}

	/**
	 * Removes update for given id. Actually item for id is just set to null so
	 * the other items ids are not changed.
	 * 
	 * @param id
	 */
	public void removeProviderStatusChangedCallback(int id)
	{
		if (id >= 0 && id < providerStatusChangedCallbacks.size())
			providerStatusChangedCallbacks.set(id, null);
	}

	/**
	 * Starts listening to location changes. Does nothing if is already
	 * listening.
	 */
	public void start()
	{
		if (isWorking)
			return;

		Log.d("LocationFinder", "started");
		List<String> providers = manager.getAllProviders();
		for (String provider : providers)
		{
			if (manager.isProviderEnabled(provider))
			{
				isWorking = true;

				manager.requestLocationUpdates(provider, 0, 0, listener);
			}
		}

		String bestProvider = manager.getBestProvider(new Criteria(), true);
		if(bestProvider!=null){
			currentBestLocation = manager.getLastKnownLocation(bestProvider);

		}
	}

	/**
	 * Stops listening to location changes. Does nothing if is not listening.
	 */
	public void stop()
	{
		if (!isWorking)
			return;
		isWorking = false;
		manager.removeUpdates(listener);
	}

	/**
	 * calls stop() then start()
	 */
	public void restart()
	{
		stop();
		start();
	}

	/**
	 * Sets current location to null and calls restart()
	 */
	public void reset()
	{
		currentBestLocation = null;
		restart();
	}

	public void clearCallbacks()
	{
		locationChangedCallbacks.clear();
	}

	public Location getCurrentBestLocation()
	{
		return currentBestLocation;
	}

	public int getAccuranceTimeout()
	{
		return accuranceTimeout;
	}

	public void setAccuranceTimeout(int accuranceTimeout)
	{
		this.accuranceTimeout = accuranceTimeout;
	}

	public int getMinTime()
	{
		return minTime;
	}

	public void setMinTime(int minTime)
	{
		this.minTime = minTime;
	}

	public int getMinDistance()
	{
		return minDistance;
	}

	public void setMinDistance(int minDistance)
	{
		this.minDistance = minDistance;
	}

	public interface LocationCallback
	{

		public void exec(Location location);
	}

	public interface ProviderStatusChangedCallback
	{

		public void exec(String provider, boolean enabled);
	}

	private class LocationInterval
	{
		private long ticks;
		private long interval;
		private LocationCallback callback;

		public LocationInterval(LocationCallback callback, long interval)
		{
			this.callback = callback;
			this.interval = interval;
		}

		public void tick(Location location)
		{
			ticks = ++ticks % interval;
			if (ticks == 0)
				callback.exec(location);
		}
	}
}
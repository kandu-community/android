package com.inomma.kandu.ui.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inomma.SharedPreferencesHelper;
import com.inomma.Toaster;
import com.inomma.kandu.KanduApplication;
import com.inomma.kandu.R;
import com.inomma.kandu.Utils;
import com.inomma.kandu.model.Config;
import com.inomma.kandu.model.FormCacheManager;
import com.inomma.kandu.model.FormListCategory;
import com.inomma.kandu.model.FormListItem;
import com.inomma.kandu.model.FormSubmission;
import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.model.UserFormsHolder;
import com.inomma.kandu.server.MainSender;
import com.inomma.kandu.server.ResponseHandler;
import com.inomma.kandu.server.request.GetConfigRequest;
import com.inomma.kandu.server.request.GetIconsRequest;
import com.inomma.kandu.server.request.GetSubmissionsRequest;
import com.inomma.kandu.server.request.GetUserFormsRequest;
import com.inomma.kandu.server.responses.GetConfigResponse;
import com.inomma.kandu.server.responses.GetIconsResponse;
import com.inomma.kandu.server.responses.GetSubmissionsResponse;
import com.inomma.kandu.server.responses.GetUserFormsResponse;
import com.inomma.kandu.sqlite.SubmissionsDataSource;
import com.inomma.kandu.ui.adapters.FormListExpandableAdapter;
import com.inomma.kandu.ui.views.IconsView;
import com.inomma.utils.NetworkUtils;

public class KanduActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private int syncTasksCount;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private GoogleMap map;
	SubmissionsDataSource dataSource;
	List<FormListCategory> categories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kandu);

		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		mDrawerList.setOnChildClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		if (map != null)
			map.setMyLocationEnabled(true);

		// if (savedInstanceState == null) {
		// selectItem(0);
		// }

		new GetIconsRequest(this, new ResponseHandler<GetIconsResponse>() {

			@Override
			public void handleResponse(GetIconsResponse response) {
				((KanduApplication) getApplication()).organizationIconUrl = response
						.getOrganizationIconUrl();
				((KanduApplication) getApplication()).parnerIconUrls = response
						.getParnerIconUrls();

				((IconsView) findViewById(R.id.icons_view))
						.setIcons(((KanduApplication) getApplication()).parnerIconUrls);

			}
		}).execute();
		sync();
		try {
			String versionName = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
			getActionBar().setTitle("Kandu (" + versionName + ")");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// saveFromFile();
	// FormCacheManager.getInstance().importCache(this);

	}

	private void saveFromFile() {
		try {
			File file = new File(Environment.getExternalStorageDirectory(),
					"kandu_backup.txt");

			try {
				String str = getStringFromFile(file);
				SharedPreferencesHelper.putStringData("cachedForms", str);
			} catch (Exception ex) {
				System.out.println("ex: " + ex);
			}
		} catch (Exception e) {
			System.out.println("e: " + e);
		}

	}

	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

	public static String getStringFromFile(File fl) throws Exception {
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		// Make sure you close all streams.
		fin.close();
		return ret;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_sync).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		if (map != null) {
			Location location = map.getMyLocation();

			if (location != null) {
				LatLng myLocation = new LatLng(location.getLatitude(),
						location.getLongitude());
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
						10));
			}
		}
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_sync:
			// create intent to perform web search for this planet
			sync();
			return true;

		case R.id.saved_forms: {
			openSavedFormsActivity();
			return true;
		}
		case R.id.logout: {
			SharedPreferencesHelper.removeData("token");
			MainSender.instance.token = null;
			MainSender.instance.BASE_URL = null;
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openSavedFormsActivity() {
		Intent intent = new Intent(this, SubmissionsActivity.class);

		startActivity(intent);
	}

	ProgressDialog syncPd;

	protected void sync() {
		// if (!NetworkUtils.isNetworkAvailable()) {
		// Toaster.show(R.string.toast_network_unavailable, Toast.LENGTH_SHORT);
		// return;
		// }

		syncPd = ProgressDialog.show(this, "", "Syncing");

		new GetConfigRequest(this, new ResponseHandler<GetConfigResponse>() {

			@Override
			public void handleResponse(GetConfigResponse response) {
				final Config config = response.getConfig();
				new GetUserFormsRequest(KanduActivity.this,
						new ResponseHandler<GetUserFormsResponse>() {
							@Override
							public void handleResponse(
									GetUserFormsResponse response) {
								UserFormsHolder.newInstance(config,
										response.getUserForms());
								Log.e("KanduActivity", "syncTasksvalue: "
										+ syncTasksCount);

								downloadDataForCacheForms();
								// syncFinished();

							}

						}).execute();

			}
		}).execute();
		Log.e("KanduActivity 253", "synctaks count: " + syncTasksCount);

	}

	int cacheFormsCount;

	private void downloadDataForCacheForms() {
		if (!NetworkUtils.isNetworkAvailable()) {
			Toast.makeText(this,
					"No Network, can't update the cached submissions",
					Toast.LENGTH_SHORT).show();
			syncFinished();
			return;

		}
		List<UserForm> userForms = UserFormsHolder.getInstance()
				.getCacheForms();

		cacheFormsCount = userForms.size();
		String[] cacheFormsTables = new String[userForms.size()];
		int i = 0;
		for (UserForm userForm : userForms) {
			cacheFormsTables[i++] = userForm.getKey();
		}
		this.dataSource = new SubmissionsDataSource(this);
		dataSource.open();
		dataSource.init(cacheFormsTables);
		if (cacheFormsCount == 0) {
			syncFinished();

		}
		for (UserForm userForm : userForms) {
			new GetSubmissionsRequest(this, userForm, userForm.getUrl(),
					new ResponseHandler<GetSubmissionsResponse>() {

						@Override
						public void handleResponse(
								GetSubmissionsResponse response) {
							List<FormSubmission> formSubmissions = response
									.getFormSubmissions();

							dataSource.addSubmissions(formSubmissions);

							cacheFormsCount--;
							if (cacheFormsCount == 0) {
								syncFinished();

							}
						}
					}).execute();
			;
		}

	}

	private void syncFinished() {
		if (dataSource != null)
			dataSource.close();
		syncPd.dismiss();

		categories = Utils.formListsFromUserForms(UserFormsHolder.getInstance()
				.getUserMainForms());
		mDrawerList.setAdapter(new FormListExpandableAdapter(this, categories));

		addMarkersToTheMap();
		// addMarkersToTheMap();

	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			selectItem(groupPosition, childPosition);
			return false;
		}

	}

	private void selectItem(int groupPosition, int childPosition) {
		// update the main content by replacing fragments
		FormListItem formListItem = categories.get(groupPosition)
				.getFormListItems().get(childPosition);

		if (!formListItem.isEditItem()) {
			Intent intent = new Intent(this, FillFormActivity.class);
			intent.putExtra("userform", formListItem.getUserForm());
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, SubmissionsActivity.class);
			intent.putExtra("userform", formListItem.getUserForm());

			startActivity(intent);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class PlanetFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";

		public PlanetFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_fill_form,
					container, false);

			return rootView;
		}
	}

	private UserForm getShowOnMapUserForm() {
		for (UserForm userForm : UserFormsHolder.getInstance().getUserForms()) {
			if (userForm.getForm().isShowOnMap()) {
				return userForm;
			}
		}
		return null;
	}

	private void addMarkersToTheMap() {
		if (!NetworkUtils.isNetworkAvailable()) {
			Toaster.show(R.string.toast_network_unavailable, Toast.LENGTH_SHORT);
			return;
		}

		UserForm userForm = getShowOnMapUserForm();
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {

				FormSubmission formSubmission = markerSubmissions.get(marker);
				Intent intent = new Intent(KanduActivity.this,
						FillFormActivity.class);
				intent.putExtra("userform", formSubmission.getForm());
				intent.putExtra("submission", formSubmission);
				startActivity(intent);
			}
		});
		new GetSubmissionsRequest(this, userForm, userForm.getUrl(), 100, map
				.getMyLocation().getLatitude(), map.getMyLocation()
				.getLongitude(), new ResponseHandler<GetSubmissionsResponse>() {

			@Override
			public void handleResponse(GetSubmissionsResponse response) {
				List<FormSubmission> formSubmissions = response
						.getFormSubmissions();
				for (FormSubmission formSubmission : formSubmissions) {
					LatLng latLng = formSubmission.getCoordinates();
					if (latLng != null) {
						markerSubmissions.put(map.addMarker(new MarkerOptions()
								.position(latLng).title(
										formSubmission.toString())),
								formSubmission);

					}

				}

			}
		}).execute();
	}

	public void onToggleClicked(View view) {
		// Is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();

		if (on) {
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		} else {

			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		}
	}

	Map<Marker, FormSubmission> markerSubmissions = new HashMap<Marker, FormSubmission>();

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, KanduApplication.FLURRY_API_KEY);
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}

package com.inomma.kandu.ui.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.flurry.android.FlurryAgent;
import com.inomma.SharedPreferencesHelper;
import com.inomma.kandu.KanduApplication;
import com.inomma.kandu.R;
import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.model.UserFormsHolder;

public class MainActivity extends Activity {

	ViewGroup mainLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainLayout = (ViewGroup)findViewById(R.id.main_layout);
		findViewById(R.id.sync_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						//sync();

					}
				});
		saveFromFile();
	}


	private void saveFromFile() {
			try {
			    File file = new File(Environment.getExternalStorageDirectory(), "kandu_backup.txt");
			   
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

	public static String getStringFromFile (File fl) throws Exception {
	    FileInputStream fin = new FileInputStream(fl);
	    String ret = convertStreamToString(fin);
	    //Make sure you close all streams.
	    fin.close();        
	    return ret;
	}

	private void syncFinished() {
		for(final UserForm userForm:UserFormsHolder.getInstance().getUserForms()){
			LinearLayout linearLayout = new LinearLayout(this);
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			Button button = new Button(this);
			button.setText(userForm.getVisibleName());
			linearLayout.addView(button);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this, FillFormActivity.class);
					intent.putExtra("userform", userForm);
					startActivity(intent);
				}
			});
			Button buttonEdit = new Button(this);
			buttonEdit.setText("Edit "+userForm.getVisibleName());
			linearLayout.addView(buttonEdit);
			buttonEdit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this, SubmissionsActivity.class);
					intent.putExtra("userform", userForm);
					startActivity(intent);
				}
			});
			mainLayout.addView(linearLayout);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
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

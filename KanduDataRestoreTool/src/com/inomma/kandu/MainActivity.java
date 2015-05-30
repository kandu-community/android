package com.inomma.kandu;

import java.io.File;
import java.io.FileWriter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;

import com.inomma.SharedPreferencesHelper;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferencesHelper.init(this);

		backup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
	private void backup() {

	

		
			String savedFormsStr = SharedPreferencesHelper.getStringData(
					"cachedForms", "");
			saveToFile(savedFormsStr);
			/* Fill it with Data */
			
		
	}

	private void saveToFile(String savedFormsStr) {
		try {
			File newFolder = new File(
					Environment.getExternalStorageDirectory(), "kandu_backup");
			if (!newFolder.exists()) {
				newFolder.mkdir();
			}
			try {
				File file = new File(newFolder, "kandu_backup" + ".txt");
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				FileWriter fw = new FileWriter(file);
				fw.write(savedFormsStr);
				;
				fw.close();
			} catch (Exception ex) {
				System.out.println("ex: " + ex);
			}
		} catch (Exception e) {
			System.out.println("e: " + e);
		}
	}
}

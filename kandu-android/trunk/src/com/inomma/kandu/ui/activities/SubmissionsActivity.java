package com.inomma.kandu.ui.activities;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.thinkti.android.filechooser.FileChooser;

import com.flurry.android.FlurryAgent;
import com.inomma.SharedPreferencesHelper;
import com.inomma.kandu.KanduApplication;
import com.inomma.kandu.R;
import com.inomma.kandu.model.FormCacheManager;
import com.inomma.kandu.model.FormSubmission;
import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.server.FormSubmitter;
import com.inomma.kandu.server.FormSubmitter.FormSubmissionListener;
import com.inomma.kandu.server.ResponseHandler;
import com.inomma.kandu.server.request.GetSubmissionsRequest;
import com.inomma.kandu.server.responses.GetSubmissionsResponse;
import com.inomma.kandu.ui.views.FormItemAutocompleteView;
import com.inomma.kandu.ui.views.FormItemAutocompleteView.ItemSelectedListener;

public class SubmissionsActivity extends Activity {
	private UserForm userForm;
	private List<FormSubmission> formSubmissions = new ArrayList<FormSubmission>();
	private int threshold = 6;
	private FormItemAutocompleteView formItemAutocompleteView;
	private ListView submissionsList;
	private ProgressDialog pd;
	private JSONArray jsonArray = new JSONArray();
	private String formsErrorMsgsStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submissions);
		submissionsList = (ListView) findViewById(R.id.submissions_list);

		if (getIntent().getExtras() != null) {
			userForm = (UserForm) getIntent().getExtras().getSerializable(
					"userform");
			formItemAutocompleteView = new FormItemAutocompleteView(this, null,
					userForm);
			((ViewGroup) findViewById(R.id.autocomplete_view_holder))
					.addView(formItemAutocompleteView);
			formItemAutocompleteView
					.setItemSelectedListener(new ItemSelectedListener() {

						@Override
						public void onItemSelected(FormSubmission formSubmission) {
							formItemAutocompleteView.setValue(null);
							Intent intent = new Intent(
									SubmissionsActivity.this,
									FillFormActivity.class);
							intent.putExtra("userform", userForm);
							intent.putExtra("submission", formSubmission);
							startActivity(intent);

						}
					});
			loadSubmissions();
		} else {
			loadSavedForms();
		}
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void loadSavedForms() {
		formSubmissions = FormCacheManager.getInstance().getAllSavedForms();
		showList();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (this.userForm == null) {
			loadSavedForms();
		}
	}

	private void loadSubmissions() {
		new GetSubmissionsRequest(this, userForm, userForm.getUrl(),
				new ResponseHandler<GetSubmissionsResponse>() {

					@Override
					public void handleResponse(GetSubmissionsResponse response) {

						formSubmissions = response.getFormSubmissions();
						addSavedSubmissions();
						showList();

					}

				}).execute();

	}

	private void addSavedSubmissions() {
		String formSubmissionsStr = SharedPreferencesHelper.getStringData(
				userForm.getKey(), null);
		if (formSubmissionsStr == null) {
			return;
		}
		try {
			JSONArray savedForms = new JSONArray(formSubmissionsStr);
			for (int i = 0; i < savedForms.length(); i++) {
				JSONObject savedForm = savedForms.getJSONObject(i);

				FormSubmission formSubmission = new FormSubmission(userForm,
						savedForm);
				formSubmission.setSubmitted(false);
				formSubmissions.add(0, formSubmission);
			}
		} catch (Exception e) {
			final Intent emailIntent = new Intent(
					android.content.Intent.ACTION_SEND);

			/* Fill it with Data */
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { "to@email.com" });
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Saved form loading error");
			emailIntent
					.putExtra(
							android.content.Intent.EXTRA_TEXT,
							formSubmissionsStr + "\n\n\n\n*****\n\n\n"
									+ e.getMessage());

			/* Send it off to the Activity-Chooser */
			this.startActivity(Intent
					.createChooser(emailIntent, "Send mail..."));
			e.printStackTrace();
		}
	}

	protected void showList() {
		ArrayAdapter<FormSubmission> adapter = new ArrayAdapter<FormSubmission>(
				SubmissionsActivity.this, android.R.layout.simple_list_item_1,
				formSubmissions);
		submissionsList.setAdapter(adapter);
		submissionsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
									long arg3) {
				Intent intent = new Intent(SubmissionsActivity.this,
						FillFormActivity.class);
				intent.putExtra("userform", formSubmissions.get(pos).getForm());

				intent.putExtra("submission", formSubmissions.get(pos));
				startActivityForResult(intent, 1000);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == 2000) && (resultCode == -1)) {
			String fileSelected = data.getStringExtra("fileSelected");
			FormCacheManager.getInstance().importCache(this, fileSelected);
			Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
			return;
		}
		if (requestCode == 1000 && resultCode == 1000) {
			if (userForm == null) {
				FormSubmission submission = (FormSubmission) data
						.getSerializableExtra("submission");
				Integer id = data.getIntExtra("id", 0);

				for (FormSubmission formSubmission : formSubmissions) {

					boolean isChanged = formSubmission
							.replaceAllSavedSubmissions(submission, id);
					if (isChanged)
						FormCacheManager.getInstance().replaceInCache(
								formSubmission);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.submissions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.

		// Handle action buttons
		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.backup:
			backup();
			return true;
		case R.id.submit_all:
			pd = ProgressDialog.show(this, "Submitting", "Please wait, process of submitting can take some time!");
			submitAll();
			return true;

		case R.id.import_from_file:
			importFromFile();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void importFromFile() {
		Intent intent = new Intent(this, FileChooser.class);
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add(".txt"); // can be used for multiple filters
		intent.putStringArrayListExtra("filterFileExtension", extensions);
		startActivityForResult(intent, 2000);
	}

	int jobCount;
	int formsProcessed = 0;

	private void submitAll() {
		if (userForm != null) {
			return;
		}
		jobCount = formSubmissions.size();
		int goupto = (formSubmissions.size() > threshold) ? threshold : formSubmissions.size();
		processForms(goupto);

	}

	private void processForms(int goupto) {
		for (int i = 0; i < goupto; i++) {
			FormSubmission formSubmission = formSubmissions.get(i);

			// send form to server
			FormSubmitter formSubmitter = new FormSubmitter(formSubmission, this,
					new FormSubmissionListener() {

						@Override
						public void formSubmitted(String errorMessage,
												  FormSubmission formSubmission, Integer id) {
							if (formSubmission != null
									&& formSubmission.getId() == null
									&& formSubmission.getUniqueId() != null) {

								formSubmissions.remove(formSubmission);
								formsProcessed++;
								jobCount--;
								pd.setMessage(jobCount + " remaining");
								if (formsProcessed >= threshold) {
									formsProcessed = 0;
									int goupto = (formSubmissions.size() > threshold) ? threshold : formSubmissions.size();
									processForms(goupto);
								}

								if (errorMessage != null) {
									formsErrorMsgsStr = formsErrorMsgsStr + formSubmission + ":\n" + errorMessage + "\n";
									try {
										JSONObject formSubmissionJson = null;
										formSubmissionJson = formSubmission.toJson();
										jsonArray.put(formSubmissionJson);

									} catch (JSONException e) {
										e.printStackTrace();
									}
								}

								if (jobCount <= 0) {
									pd.dismiss();
									//replace saved froms with fromsWithError in cache
									// show errors in alert
									if (formsErrorMsgsStr != null && !formsErrorMsgsStr.isEmpty()) {
										new AlertDialog.Builder(SubmissionsActivity.this)
												.setTitle("Errors")
												.setMessage("There are some errors in forms, Kindly correct them: \n" + formsErrorMsgsStr)
												.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int which) {
														// continue with delete
													}
												})
												.setIcon(android.R.drawable.ic_dialog_alert)
												.show();
									}

									SharedPreferencesHelper.putStringData("cachedForms",
											jsonArray.toString());

									jsonArray = new JSONArray();
									formsProcessed = 0;
									formsErrorMsgsStr = "";

									loadSavedForms();

									return;
								}
							}
						}

					});

			formSubmitter.submitForm();
		}
	}

	private void backup() {

		if (userForm != null) {
			Toast.makeText(this, "You can backup only saved forms",
					Toast.LENGTH_SHORT).show();
		} else {

			final Intent emailIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			String savedFormsStr = SharedPreferencesHelper.getStringData(
					"cachedForms", "");
			saveToFile(savedFormsStr);
			/* Fill it with Data */
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { "narek@inomma.com", "qholloi@gmail.com" });
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Backed up data");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					savedFormsStr);

			/* Send it off to the Activity-Chooser */
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		}
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

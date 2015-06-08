package com.inomma.kandu.ui.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.inomma.Toaster;
import com.inomma.kandu.KanduApplication;
import com.inomma.kandu.R;
import com.inomma.kandu.model.FormCacheManager;
import com.inomma.kandu.model.FormItem;
import com.inomma.kandu.model.FormItemType;
import com.inomma.kandu.model.FormSubmission;
import com.inomma.kandu.model.FormSubmissionItem;
import com.inomma.kandu.model.SmallFormSubmission;
import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.model.UserFormsHolder;
import com.inomma.kandu.server.FormSubmitter;
import com.inomma.kandu.server.FormSubmitter.FormSubmissionListener;
import com.inomma.kandu.server.request.GetSubmissionsRequest;
import com.inomma.kandu.server.responses.GetSubmissionsResponse;
import com.inomma.kandu.sqlite.SubmissionsDataSource;
import com.inomma.kandu.ui.views.FormView;

public class FillFormActivity extends Activity {

	private UserForm form;

	private ViewGroup mainLayout;

	private FormSubmission formSubmission;
	private List<FormView> inlineForms = new ArrayList<FormView>();
	private UserForm inlineForm;
	private FormView mainView;
	FormSubmission finalformSubmission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fill_form);
		this.form = (UserForm) getIntent().getExtras().getSerializable(
				"userform");
		if (getIntent().getExtras().containsKey("submission")) {

			this.formSubmission = (FormSubmission) getIntent().getExtras()
					.getSerializable("submission");
			this.form = this.formSubmission.getForm();

		}
		this.mainLayout = (ViewGroup) findViewById(R.id.main_layout);
		mainView = new FormView(this);
		mainView.setData(
				form,
				formSubmission != null ? formSubmission
						.getSmallFormSubmission() : null);
		this.mainLayout.addView(mainView);
		fillInlineForms();
		getActionBar().setTitle(this.form.getVisibleName());
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	private void fillInlineForms() {
		for (UserForm inline : form.getInlineForms()) {
			inlineForm = inline;
			if (formSubmission != null) {
				for (SmallFormSubmission inlineSmallFormSubmission : formSubmission
						.getInlineFormSubmissions()) {
					FormView inlineFormView = new FormView(this);
					inlineFormView.setInline(true);

					inlineFormView.setData(inline, inlineSmallFormSubmission);
					inlineForms.add(inlineFormView);
					mainLayout.addView(inlineFormView);
				}
			}
			if (inlineForms.size() == 0) {
				FormView inlineFormView = new FormView(this);
				inlineFormView.setInline(true);

				inlineFormView.setData(inline, null);
				inlineForms.add(inlineFormView);
				mainLayout.addView(inlineFormView);
			}

			break;// TODO: supports only 1 inline form now
		}
		Button button = new Button(this);
		button.setText("Add another");
		mainLayout.addView(button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FormView inlineFormView = new FormView(FillFormActivity.this);
				inlineFormView.setInline(true);

				inlineFormView.setData(inlineForm, null);
				inlineForms.add(inlineFormView);
				mainLayout.addView(inlineFormView,
						mainLayout.getChildCount() - 1);

			}
		});
		if (inlineForms.size() == 0) {
			button.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fill_form, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.

		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_submit:
			// create intent to perform web search for this planet
			showSaveChoices();
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showSaveChoices() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select date");
		builder.setItems(new String[] { "Save and Submit", " Save Only" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							showSubmitGPSAlert(false);
						} else {
							showSubmitGPSAlert(true);
						}
					}

				});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	// private void saveOnly() {
	// // Map<String, Object> params = mainView.getParams();
	// // if (params == null) {
	// // return;
	// // }
	// // try {
	// // JSONObject jsonObject = Utils.mapToJsonObject(params);
	// //
	// // for (FormView inlineFormView : inlineForms) {
	// // Map<String, Object> inlineParams = inlineFormView.getParams();
	// // if (params == null) {
	// // return;
	// // }
	// // JSONObject inlineJsonObject = Utils
	// // .mapToJsonObject(inlineParams);
	// // jsonObject.put(inlineForm.getKey(), inlineJsonObject);
	// //
	// // }
	// //
	// // String formSubmissions = SharedPreferencesHelper.getStringData(
	// // form.getKey(), null);
	// // JSONArray savedForms = null;
	// // if (formSubmissions != null) {
	// // savedForms = new JSONArray(formSubmissions);
	// // } else {
	// // savedForms = new JSONArray();
	// // }
	// // savedForms.put(jsonObject);
	// // SharedPreferencesHelper.putStringData(form.getKey(),
	// // savedForms.toString());
	// // Toaster.show("Saved succesfull", Toast.LENGTH_SHORT);
	// // finish();
	// //
	// // } catch (JSONException e) {
	// // e.printStackTrace();
	// // }
	//
	//
	// // finish();
	// }
	//
	private void saveOnly(boolean includeLocation) {
		FormSubmission formSubmission = getFinalFormSubmission(includeLocation);

		if (formSubmission == null) {
			return;
		}

		if (formSubmission.getUniqueId() != null) {
			FormCacheManager.getInstance().replaceInCache(formSubmission);

		} else {
			FormCacheManager.getInstance().addToCache(formSubmission);

		}
		addToDb(formSubmission);

		Toaster.show("Saved succesfully", Toast.LENGTH_SHORT);
		finish();
	}

	private void addToDb(FormSubmission formSubmission) {
		if (formSubmission.getId() != null) {
			return;
		}
		SubmissionsDataSource dataSource = new SubmissionsDataSource(this);
		dataSource.open();
		dataSource.addSubmission(formSubmission);
		dataSource.close();
	}

	public void submit(boolean includeLocation) {
		finalformSubmission = getFinalFormSubmission(includeLocation);
		if (finalformSubmission == null) {
			return;
		}
		addToDb(finalformSubmission);
		List<FormSubmission> formsToSubmit = checkForUnsubmittedForms(finalformSubmission);
		if (formsToSubmit.size() == 0) {
			submitFinalForm();
		} else {
			showUnsubmittedFormsAlert(formsToSubmit);
			//submitUnsubmittedForms(formsToSubmit);
		}
		// Map<String, Object> params = mainView.getParams();
		// if (params == null) {
		// return;
		// }
		//
		// if (!NetworkUtils.isNetworkAvailable()) {
		// Toaster.show("Network is not available, please do save only",
		// Toast.LENGTH_SHORT);
		// return;
		// }
		//
		// new SubmitFormRequest(this, form.getUrl(), params,
		// new ResponseHandler<SubmitFormResponse>() {
		//
		// @Override
		// public void handleResponse(SubmitFormResponse response) {
		// if (response.id == null) {
		// Toast.makeText(FillFormActivity.this,
		// "Server error", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// if (inlineForms.size() != 0) {
		// submitInlineForms(response.id);
		//
		// } else {
		// Toast.makeText(FillFormActivity.this, "Submitted",
		// Toast.LENGTH_SHORT).show();
		// finish();
		// }
		//
		// }
		// }).execute();

	}

	private void showUnsubmittedFormsAlert(final List<FormSubmission> formsToSubmit) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
		.setMessage(
				"There are some forms that need to submit before you can submit the current one, do you want to submit them automatically?")
		.setNegativeButton("No",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						
					}
				})
		.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						submitUnsubmittedForms(formsToSubmit);

					}
				}).show();
	}

	private void submitFinalForm() {
		new FormSubmitter(finalformSubmission, this,
				new FormSubmissionListener() {

					@Override
					public void formSubmitted(String errorMessage,FormSubmission form, Integer id) {
						if (errorMessage == null) {
							if (formSubmission != null
									&& formSubmission.getId() == null
									&& formSubmission.getUniqueId() != null) {
								FormCacheManager.getInstance().deleteFromCache(
										formSubmission);
								;
							}
							Toast.makeText(FillFormActivity.this, "Submitted",
									Toast.LENGTH_SHORT).show();
							Intent returnIntent = new Intent();
							returnIntent.putExtra("id",id);
							returnIntent.putExtra("submission", form);
							setResult(1000,returnIntent);
							//setResult(1000, new In);
							finish();
						} else {
							Toast.makeText(FillFormActivity.this, errorMessage,
									Toast.LENGTH_SHORT).show();
						}

					}
				}).submitForm();
	}

	int jobCount = 0;

	private void submitUnsubmittedForms(List<FormSubmission> formsToSubmit) {
		jobCount = formsToSubmit.size();
		for (final FormSubmission formSubmission : formsToSubmit) {
			new FormSubmitter(formSubmission, this,
					new FormSubmissionListener() {

						@Override
						public void formSubmitted(String errorMessage,FormSubmission submission,
								Integer id) {
							if (errorMessage != null) {
								Toast.makeText(FillFormActivity.this,
										errorMessage, Toast.LENGTH_SHORT)
										.show();
								return;
							}
							SubmissionsDataSource dataSource = new SubmissionsDataSource(
									FillFormActivity.this);
							dataSource.open();
							dataSource.removeSubmission(submission);
							dataSource.close();
							FormCacheManager.getInstance().deleteFromCache(
									submission);
							finalformSubmission
									.getSmallFormSubmission()
									.getFormSubmissionItem(
											submission.getLinkedTo())
									.setValue(id + "");
							if (--jobCount == 0) {
								submitFinalForm();
							}
						}
					}).submitForm();; 

		}

	}

	private List<FormSubmission> checkForUnsubmittedForms(
			FormSubmission finalformSubmission) {
		// TODO Auto-generated method stub

		List<FormSubmission> formsToSubmit = new ArrayList<FormSubmission>();
		SmallFormSubmission smallFormSubmission = finalformSubmission
				.getSmallFormSubmission();
		for (FormSubmissionItem formSubmissionItem : smallFormSubmission
				.getFormSubmissionItems()) {
			FormItem formItem = smallFormSubmission.getUserForm()
					.getFormItemByKey(formSubmissionItem.getKey());
			if (formItem.getFormItemType() == FormItemType.FOREIGN_KEY) {
				try {

					final FormSubmission formSubmission = new FormSubmission(
							UserFormsHolder.getInstance().getUserFormWithKey(
									formItem.getLinkTo()), new JSONObject(
									formSubmissionItem.getValue()));
//					Integer id = formSubmission.getId();
					if (!formSubmissionItem.isValueSubmitted(formItem)) {
						formSubmission.setLinkedTo(formItem.getKey());
						formsToSubmit.add(formSubmission);
					}
				} catch (Exception e) {

				}
			}
		}

		return formsToSubmit;

	}

	protected void submitInlineForms(int id) {
		// for (FormView inlineFormView : inlineForms) {
		// Map<String, Object> params = inlineFormView.getParams();
		// if (params == null) {
		// return;
		// }
		// params.put(form.getKey(), id + "");
		//
		// new SubmitFormRequest(this, inlineForm.getUrl(), params,
		// new ResponseHandler<SubmitFormResponse>() {
		//
		// @Override
		// public void handleResponse(SubmitFormResponse response) {
		// Toast.makeText(FillFormActivity.this, "Submitted",
		// Toast.LENGTH_SHORT).show();
		// finish();
		//
		// }
		// }).execute();
		// }
		//

	}

	private void showSubmitGPSAlert(final boolean saveOnly) {
		if (mainView.hasLocation()) {
			new AlertDialog.Builder(this)
					.setMessage(
							"Do you want to submit your current location to the form")
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (saveOnly) {
										saveOnly(false);
									} else {
										submit(false);
									}
								}
							})
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (saveOnly) {
										saveOnly(true);
									} else {
										submit(true);

									}
								}
							}).show();
		} else {
			if (saveOnly) {
				saveOnly(false);
			} else {
				submit(false);
			}
		}
	}

	private FormSubmission getFinalFormSubmission(boolean updateLocation) {
		SmallFormSubmission mainFormSubmission = mainView
				.getSubmission(updateLocation);
		if (mainFormSubmission == null) {
			return null;
		}
		FormSubmission formSubmission = new FormSubmission(this.form);

		formSubmission.setSmallFormSubmission(mainFormSubmission);

		for (FormView inlineFormView : inlineForms) {
			SmallFormSubmission inlineFormSubmission = inlineFormView
					.getSubmission(true);
			formSubmission.addInlineSmallFormSubmission(inlineFormSubmission);
		}
		if (this.formSubmission != null
				&& this.formSubmission.getUniqueId() != null) {
			formSubmission.setUniqueId(this.formSubmission.getUniqueId());
		}
		return formSubmission;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			mainView.onActivityResult(requestCode, resultCode, data);
		} catch (ClassCastException e) {
			e.printStackTrace();
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

	public FormView getMainView() {
		return mainView;
	}

}

/**
 * 
 */
package com.inomma.kandu.ui.activities.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.junit.Test;

import com.inomma.kandu.model.Config;
import com.inomma.kandu.model.FormSubmissionItem;
import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.model.UserFormsHolder;
import com.inomma.kandu.ui.activities.FillFormActivity;
import com.inomma.kandu.ui.views.FormItemChoiceView;
import com.inomma.kandu.ui.views.FormItemSingleChoiceView;
import com.inomma.kandu.ui.views.FormItemView;
import com.inomma.kandu.ui.views.FormView;
import com.nostra13.universalimageloader.utils.L;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author marlinf
 *
 */
public class FillFormActivityTest extends ActivityInstrumentationTestCase2<FillFormActivity> {
	private FillFormActivity activity;
	private FormView mainView;
	
	public FillFormActivityTest() {
		super(FillFormActivity.class);
	}

	private String convertStreamToString(java.io.InputStream is) {
	    Scanner s = new Scanner(is).useDelimiter("\\A");
	    String string = s.hasNext() ? s.next() : "";
	    s.close();
	    return string;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();

		Context context = getInstrumentation().getContext();
		Resources resources = context.getResources();
		
		InputStream stream = resources.openRawResource(com.inomma.kandu.test.R.raw.config);
		String json = convertStreamToString(stream);
		
		JSONArray forms = new JSONArray(json);
		Config config = new Config(forms);

		List<UserForm> userForms = new ArrayList<UserForm>();
		
		UserForm userForm = new UserForm();
		userForm.setUrl("/api/getSubmissions/Test_Form/");
		userForm.setVisibleName("Test Form");
		userForm.setKey("Test_Form");
		
		userForms.add(userForm);
		
		UserFormsHolder.newInstance(config, userForms);
		
		UserForm form = UserFormsHolder.getInstance().getUserFormWithKey("Test_Form");

		Intent intent = new Intent();
		intent.putExtra("userform", form);
		
		setActivityInitialTouchMode(true);
		setActivityIntent(intent);
		
		activity = getActivity();
		mainView = activity.getMainView();
	}
	
	@Test
	public void testViewItems()
	{
		FormItemSingleChoiceView choiceView = (FormItemSingleChoiceView) mainView.getViews().get("Array_config");
		
		Spinner spinner = choiceView.getSpinner();
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
		
		assertEquals("Adapter should have 3 items", 3, adapter.getCount());
		assertEquals("First key should be a__b", "a__b", adapter.getItem(1));
		assertEquals("Second key should be a", "a", adapter.getItem(2));
		
		/** TODO: Get this working */

		/*
		TouchUtils.clickView(this, choiceView);
		TouchUtils.clickView(this, choiceView.getChildAt(2));
		assertEquals("Should be a when set to a", "a", choiceView.getValue());
		*/
	}
}

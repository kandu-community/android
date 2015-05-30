package com.inomma.kandu.ui.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.inomma.kandu.Utils;
import com.inomma.kandu.model.FormItem;
import com.inomma.kandu.model.FormSubmissionItem;
import com.inomma.kandu.ui.views.MultiSpinner.MultiSpinnerListener;

public class FormItemMultiChoiceView extends FormItemChoiceView {

	private MultiSpinner multiSpinner;

	public FormItemMultiChoiceView(Context context) {
		super(context);
	}

	public FormItemMultiChoiceView(Context context, FormItem item) {
		super(context, item);
	}

	public FormItemMultiChoiceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FormItemMultiChoiceView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void fillContent(Context context) {
		super.fillContent(context);
		multiSpinner = new MultiSpinner(context);
		multiSpinner.setItems(Arrays.asList(item.getChoices()), "All Selected",
				item.getHint(), new MultiSpinnerListener() {

					@Override
					public void onItemsSelected(boolean[] selected) {
						List<String> selectedChoices = new ArrayList<String>();
						int i = 0;
						for (String choice : item.getChoices()) {
							if (selected[i++]) {
								selectedChoices.add(choice);
							}
						}
						String[] choicesStr = new String[selectedChoices.size()];
						int j = 0;
						for (String selectedChoice : selectedChoices) {
							choicesStr[j++] = selectedChoice;
						}
						onChoiceChanged(choicesStr);
					}
				});
		addView(multiSpinner);
		if(showDefaultValue()){
			multiSpinner.getState()[0] = true;
		}

	}

	@Override
	public void setValue(FormSubmissionItem value) {
		if (value != null) {
			
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(value.getValue());
				for (int i = 0; i < jsonArray.length(); i++) {
					String selection = jsonArray.getString(i);
					multiSpinner.getState()[getChoiceIntex(selection)] = true;
				}
			} catch (JSONException e) {
				try{
					String[] values = value.getValue().split(",");
					for (String selection: values) {
						multiSpinner.getState()[getChoiceIntex(selection)] = true;
					}
				}
				catch(Exception ex){
					
				}
				e.printStackTrace();
			}
			
		}

	}

	@Override
	public Object getValue() {
		boolean[] state = multiSpinner.getState();
		ArrayList<String> results = new ArrayList<String>();
		String[] choices = item.getChoices();
		for (int i = 0; i < state.length; i++) {
			if (state[i])
				results.add(Utils.keyFromName(choices[i]));
		}
		return results.isEmpty() ? null : results;
	}

	@Override
	public String getValueString() {
		return null;
	}

	@Override
	public void addVisibility(String choice, View view) {

		super.addVisibility(choice, view);
		onChoiceChanged();

	}
}

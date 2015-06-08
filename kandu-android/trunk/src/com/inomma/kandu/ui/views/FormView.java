package com.inomma.kandu.ui.views;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inomma.Toaster;
import com.inomma.kandu.Utils;
import com.inomma.kandu.model.FormItem;
import com.inomma.kandu.model.FormSubmissionItem;
import com.inomma.kandu.model.SmallFormSubmission;
import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.model.UserFormsHolder;
import com.inomma.utils.location.AdvancedLocationManager;

public class FormView extends LinearLayout {

	private UserForm form;

	private Map<String, FormItemView> views = new HashMap<String, FormItemView>();
	private FormItem locationFormItem;
	private SmallFormSubmission smallFormSubmission;
	private boolean isInline;

	public FormView(Context context) {
		super(context);
		fillContent();
	}

	public FormView(Context context, AttributeSet attrs) {
		super(context, attrs);
		fillContent();
	}

	public FormView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		fillContent();
	}

	private void fillContent() {
		setOrientation(VERTICAL);
	}

	public void setData(UserForm userForm,
			SmallFormSubmission smallFormSubmission) {
		this.form = userForm;
		this.smallFormSubmission = smallFormSubmission;
		createFields();
	}

	private void createFields() {
		List<FormItem> items = form.getForm().getFormItems();
		for (FormItem item : items) {
			FormItemView v = getViewForItem(item);
			if (v == null)
				continue;
			addView(v);
			views.put(item.getKey(), v);

			if (item.getVisibleWhen() != null) {
				Map<String, String> visibleWhen = item.getVisibleWhen();
				for (String key : visibleWhen.keySet()) {
					View visibleWhenView = views.get(Utils.keyFromName(key));
					if (visibleWhenView instanceof FormItemChoiceView) {
						((FormItemChoiceView) visibleWhenView).addVisibility(
								visibleWhen.get(key), v);
					}
				}
			}
		}

	}

	private FormItemView getViewForItem(FormItem item) {
		FormItemView view = null;

		switch (item.getFormItemType()) {
		case CHOICE:
			view = new FormItemSingleChoiceView(getContext(), item);
			break;
		case TEXT:
			view = new FormItemTextView(getContext(), item);
			break;
		case NUMBER:
			view = new FormItemNumberView(getContext(), item);
			break;
		case DECIMAL:
			view = new FormItemDecimalView(getContext(), item);
			break;
		case MULTICHOICE:
			view = new FormItemMultiChoiceView(getContext(), item);
			break;
		case GPS:
			startGpsTracking(item);
			break;
		case IMAGE:
			view = new FormItemFileView((Activity) getContext(), item);// TODO:
																		// needs
																		// better
			break;
		case FOREIGN_KEY:
			if (!isInline) {
				view = new FormItemAutocompleteView(getContext(), item,
						UserFormsHolder.getInstance().getUserFormWithKey(
								item.getLinkTo()));

			}

			break;
		case MANY_TO_MANY:
			//if (!isInline) {
				view = new FormItemManyToManyView(getContext(), item,
						UserFormsHolder.getInstance().getUserFormWithKey(
								item.getLinkTo()));

			//}

			break;
		case DATE: {
			view = new FormItemDateView(getContext(), item);
			break;
		}
		default:

			break;
		}
		if (smallFormSubmission != null && view != null) {
			FormSubmissionItem formSubmissionItem = smallFormSubmission
					.getFormSubmissionItem(item.getKey());
			if (formSubmissionItem != null)
				view.setValue(formSubmissionItem);
		}

		return view;
	}

	private void startGpsTracking(FormItem locationFormItem) {
		this.locationFormItem = locationFormItem;
		AdvancedLocationManager.getInstance().start();
	}

	public boolean hasLocation() {
		return locationFormItem != null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SmallFormSubmission getSubmission(boolean updateLocation) {
		SmallFormSubmission smallFormSubmission = new SmallFormSubmission(
				this.form);
		for (FormItemView view : views.values()) {
			FormItem formItem = ((FormItem) view.getTag());
			FormSubmissionItem formSubmissionItem = new FormSubmissionItem();
			formSubmissionItem.setKey(formItem.getKey());
			Object value = view.getValue();

			if (formItem.isRequired() && value == null) {
				Toaster.show(formItem.getVisibleName() + " is required",
						Toast.LENGTH_SHORT);
				return null;
			}
			String errorMessage = formItem.validate(value);
			if(errorMessage!=null){
				Toaster.show(errorMessage,
						Toast.LENGTH_SHORT);
				return null;
			}
			if (value == null) {
				continue;
			}
			if (value instanceof File) {
				formSubmissionItem.setValue(((File) value).getPath());
				formSubmissionItem.setIsLocalFile(true);
			} else if (value instanceof List) {
				formSubmissionItem.setValue(Utils.stringFromList((List) value));
			} else {
				formSubmissionItem.setValue(value.toString());
			}
			smallFormSubmission.putFormSubmissionItem(formItem.getKey(),
					formSubmissionItem);
		}
		if (locationFormItem != null) {
			FormSubmissionItem savedLocation = this.smallFormSubmission != null ? this.smallFormSubmission
					.getFormSubmissionItem(locationFormItem.getKey()) : null;
			if(updateLocation){
				Location location = AdvancedLocationManager.getInstance()
						.getCurrentBestLocation();
				if (location != null) {
					FormSubmissionItem formSubmissionItem = new FormSubmissionItem();
					formSubmissionItem.setKey(locationFormItem.getKey());
					String locStr = location.getLatitude() + ","
							+ location.getLongitude();
					formSubmissionItem.setValue(locStr);
					smallFormSubmission.putFormSubmissionItem(
							formSubmissionItem.getKey(), formSubmissionItem);

				}
			}else if (savedLocation != null) {
				if(savedLocation.getValue()==null || savedLocation.getValue().equals("null")){
					savedLocation.setValue("");;//TODO://needs to do for all maybe
				}
				smallFormSubmission.putFormSubmissionItem(
						locationFormItem.getKey(), savedLocation);
			}
			
		}
		if (this.smallFormSubmission != null) {
			smallFormSubmission.setId(this.smallFormSubmission.getId());
		}
		return smallFormSubmission;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			FormItemFileView view = (FormItemFileView) findViewById(requestCode);
			view.onActivityResult(requestCode, resultCode, data);
		} catch (ClassCastException e) {

		}
	}

	public boolean isInline() {
		return isInline;
	}

	public void setInline(boolean isInline) {
		this.isInline = isInline;
	}

	public Map<String, FormItemView> getViews()
	{
		return views;
	}
}

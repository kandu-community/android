package com.inomma.kandu.ui.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.inomma.kandu.model.FormSubmission;
import com.inomma.kandu.model.UserForm;
import com.inomma.kandu.server.ResponseHandler;
import com.inomma.kandu.server.request.GetSubmissionsRequest;
import com.inomma.kandu.server.responses.GetSubmissionsResponse;

public class SubmissionsAutocompleteAdapter extends
		ArrayAdapter<FormSubmission> implements Filterable {
	private List<FormSubmission> mData;
	private UserForm userForm;
	boolean isRequesting = false;

	public SubmissionsAutocompleteAdapter(Context context,
			int textViewResourceId, UserForm userForm) {
		super(context, textViewResourceId);
		mData = new ArrayList<FormSubmission>();
		this.userForm = userForm;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public FormSubmission getItem(int index) {
		return mData.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter myFilter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				final FilterResults filterResults = new FilterResults();

				if (constraint != null) {
					// A class that queries a web API, parses the data and
					// returns an ArrayList<Style>
					isRequesting = true;
					new GetSubmissionsRequest(getContext(), userForm,
							userForm.getUrl(), constraint.toString().trim(),
							new ResponseHandler<GetSubmissionsResponse>() {

								@Override
								public void handleResponse(
										GetSubmissionsResponse response) {
									
									mData = response.getFormSubmissions();
									filterResults.values = mData;
									filterResults.count = mData.size();
									isRequesting = false;

								}
							}).execute();
					;

					// Now assign the values and count to the FilterResults
					// object

				}
				while (isRequesting) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence contraint,
					final FilterResults results) {
				((Activity)getContext()).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if (results != null && results.count > 0) {
							notifyDataSetChanged();
						} else {
							notifyDataSetInvalidated();
						}						
					}
				});
				
			}
		};
		return myFilter;
	}
}
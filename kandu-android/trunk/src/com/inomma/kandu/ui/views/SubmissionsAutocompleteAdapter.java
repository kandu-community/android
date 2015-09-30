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
import com.inomma.kandu.sqlite.SubmissionsDataSource;
import com.inomma.utils.NetworkUtils;

public class SubmissionsAutocompleteAdapter extends
		ArrayAdapter<FormSubmission> implements Filterable {
	private List<FormSubmission> mData;
	private List<FormSubmission> allData;
	private UserForm userForm;
	boolean isRequesting = false;

	public SubmissionsAutocompleteAdapter(Context context,
			int textViewResourceId, UserForm userForm) {
		super(context, textViewResourceId);
		mData = new ArrayList<FormSubmission>();
		allData = new ArrayList<FormSubmission>();
		this.userForm = userForm;
		getAllData();
	}

	@Override
	public int getCount() {
		try{
		return mData.size();
		}
		catch(Exception e)
		{
			return 0;
		}
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

				if (NetworkUtils.isNetworkAvailable()
						|| !userForm.getForm().isCacheSubmissionsOffline()) {
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
				} else {
					List<FormSubmission> tempData = new ArrayList<FormSubmission>();;

					tempData.clear();
					for (int i = 0; i < allData.size(); i++) 
					{
						FormSubmission a = allData.get(i);
						if(containsIgnoreCase(a.toString(), (String) constraint))
						{
							tempData.add(a);
						}
					}
					filterResults.values = tempData;
					filterResults.count = tempData.size();
				}
				
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence contraint,
					final FilterResults results) {
				((Activity)getContext()).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mData = (List<FormSubmission>) results.values;
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
	
	public void getAllData()
	{
		 new Thread(new Runnable() {
		        public void run() {
		        	SubmissionsDataSource dataSource = new SubmissionsDataSource(
		    				getContext());
		    		dataSource.open();
		    		allData = dataSource.getAllSubmissions(userForm.getForm(), "");
		    		dataSource.close();
		        }
		    }).start();

	}
	public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches()
            // method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }
}
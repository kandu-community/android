package com.inomma.ui.fragments.tabfragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Superclass for tab fragments.
 * */
public class TabFragment extends Fragment{

    protected View content;
    protected TabContainer activity;

    
    /**
     * Initiates content and activity fields.
     * */
    public void init(LayoutInflater inflater, ViewGroup container, int layoutId) {
        content = inflater.inflate(layoutId, container, false);
        activity = (TabContainer) getActivity();
    }

    /**
     * Shows a new fragment and adds it to current tab's fragments queue.
     * @param fragment new fragment to be shown.
     * */
    protected void changeFragment(Fragment fragment) {
		activity.addFragment(fragment);
	}
    
    public void onBackPressed()
    {
    	activity.onBackPressed();
    }

    protected View findViewById(int id)
    {
        return content.findViewById(id);
    }

    /**
     * This method will be called when back button is pressed while this fragment was visible.
     * Should be overrided in subclasses for passing needed data to previous fragment.
     * @return data in Bundle object for previous fragment.*/
	public Bundle getReturnData()
	{
		return null;
	}

    /**
     * Will be called when data was passed from next fragment or activity called for result.
     * Should be overrided in subclasses for updating this fragment for appropriate data.
     * @param data data needed to update this fragment.
     * */
	public void update(Bundle data)
	{}

    /**
     * Same method as update + Uri parameter which comes with intent when an activity was called for result.
     * Should be overrided in subclasses for custom behaviour.*/
	public void update(Bundle data, Uri uri)
	{
		update(data);
	}
}

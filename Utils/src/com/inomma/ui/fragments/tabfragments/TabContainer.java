package com.inomma.ui.fragments.tabfragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * Superclass for activities containing tabs.
 * */
public class TabContainer extends FragmentActivity {
	
	protected List<OnTabChangeListener> onTabChangeListeners;
	protected TabManager tabManager;
	protected List<View> tabs;
	protected Fragment tabContent;
	protected int containerId;
	
	@Override
	protected void onCreate(Bundle data) {
		super.onCreate(data);
		onTabChangeListeners = new ArrayList<OnTabChangeListener>();
		tabs = new ArrayList<View>();
		tabManager = new TabManager();
	}
	
	public void addTabChangeListener(OnTabChangeListener listener)
	{
		onTabChangeListeners.add(listener);
	}
	
	protected void addTabClickListeners()
	{
		for(int i = 0; i < tabs.size(); i++)
		{
			final int index = i;
			tabs.get(i).setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					if(tabManager.getCurrentTabIndex() == index)
						return;
					switchTab(index);
				}
			});
		}
	}
	
	public Fragment getCurrentFragment()
	{
		return tabManager.getCurrentVisibleFragment();
	}
	
	public void switchTab(int index)
	{
		switchTab(index, null);
	}
	/**
	 * Switches tab with specified number (starts at 0)
     * Removes current tab and keeps blank tab container if no tab with given index was found.
	 */
	public void switchTab(int index, Bundle args)
	{
		int currentIndex = tabManager.getCurrentTabIndex();
		Fragment currentFragment = tabManager.getCurrentVisibleFragment();
		if(currentFragment != null)
			getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();

		Fragment fragment = tabManager.switchTab(index);
		if(fragment == null)
			return;
		if(args != null)
			fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction().replace(containerId, fragment).commit();
		if(currentIndex >= 0)
			this.tabs.get(currentIndex).setSelected(false);
		this.tabs.get(index).setSelected(true);
		
		for(OnTabChangeListener listener : onTabChangeListeners)
			listener.onTabChange(index);
	}

    /**
     * Adds a fragment to current tab's fragments queue.
     * @param fragment fragment to be added.
     * */
	public void addFragment(Fragment fragment)
	{
		if(fragment == null)
			return;

		getSupportFragmentManager().beginTransaction().hide(tabManager.getCurrentVisibleFragment()).add(containerId, fragment).commit();
		tabManager.addFragment(fragment);
	}

    /**
     * This method will be called when back button is pressed on first fragment of the tab.
     * Subclass should override this method for custom behaviour.
     * */
	protected void onFirstFragmentBackPressed()
	{
        //make a toast for example
		super.onBackPressed();
	}

    /**
     * Overrided method of activity.
     * Previous fragment is shown instead of standard back logic of android.
     * */
	@Override
	public void onBackPressed()
	{		
		Fragment current = tabManager.popFragment();
		if(current == null)
		{
			onFirstFragmentBackPressed();
			return;
		}
		Fragment fragment = tabManager.getCurrentVisibleFragment();
		
		if(fragment instanceof TabFragment && current instanceof TabFragment)
			((TabFragment)fragment).update(((TabFragment)current).getReturnData());

		getSupportFragmentManager().beginTransaction().remove(current).commit();
		getSupportFragmentManager().beginTransaction().show(fragment).commit();
	}
	
	/**
     * Adds a tab to tabs.
     * @param viewId switcher button id for tab.
     * @param fragment first fragment of new tab.
     * */
	public void addTab(int viewId, Fragment fragment)
	{
		addTab(findViewById(viewId), fragment);
	}

    /**
     * Adds a tab to tabs.
     * @param v switcher button for tab.
     * @param fragment first fragment of new tab.
     * */
	public void addTab(View v, Fragment fragment)
	{
		tabs.add(v);
		tabManager.addTab(fragment);
	}

    /**
     * Overrided method of activity.
     * This will be called when another activity was started for result from one of fragments.
     * Note that startActivityForResult must be called for this activity (fragment's parent activity), not fragment. Otherwise onActivityResul of fragment will be called.
     * Updates fragment for data from activity result.*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		Bundle params = (data != null && data.getExtras() != null) ? data.getExtras() : new Bundle();
		params.putInt("requestCode", requestCode);
		params.putInt("resultCode", resultCode);
		Fragment currentVisible = tabManager.getCurrentVisibleFragment();
		if(currentVisible instanceof TabFragment)
			((TabFragment) currentVisible).update(params, data == null ? null : data.getData());
	}
}
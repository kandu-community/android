package com.inomma.ui.fragments.tabfragments;

import java.util.ArrayList;
import java.util.Stack;

import android.support.v4.app.Fragment;


public class TabManager {

	private ArrayList<Stack<Fragment>> stacks;
	private int currentTabIndex;
	
	public TabManager()
	{
		stacks = new ArrayList<Stack<Fragment>>();
		currentTabIndex = -1;
	}
	
	public int getCurrentTabIndex()
	{
		return currentTabIndex;
	}

	/**
	 * Adds a new tab with given default fragment.
	 * @param defaultFragment
	 * default fragment of the tab
	 */
	public void addTab(Fragment defaultFragment)
	{
		this.addTab(stacks.size(), defaultFragment);
	}
	
	/**
	 * Adds a new tab to specified index with given default fragment.
	 * @param index
	 * new tab's index.
	 * @param defaultFragment
	 * default fragment of the tab
	 */
	public void addTab(int index, Fragment defaultFragment)
	{
		Stack<Fragment> newTabStack = new Stack<Fragment>();
		newTabStack.push(defaultFragment);
		stacks.add(index, newTabStack);
	}
	
	public void clearCurrentTabStack()
	{
		if(currentTabIndex >= 0)
			this.clearTabStack(currentTabIndex);
	}
	
	public void clearTabStack(int index)
	{
		if(index >= stacks.size() || index < 0)
			return;
		
		Stack<Fragment> s = stacks.get(index);
		Fragment firstElement = s.get(0);
		s.clear();
		s.push(firstElement);
	}
	
	public void clearAllTabStacks()
	{
		for(int i = 0 ; i < stacks.size(); i++)
			clearTabStack(i);
	}
	
	public Fragment switchTab(int index)
	{
		if(index >= stacks.size() || index < 0)
			return null;
		
		clearCurrentTabStack();
		currentTabIndex = index;
		
		return stacks.get(index).get(0);
	}
	
	/**
	 * @param fragment to push
	 * @return the current visible fragment before pushing.
	 */
	public void addFragment(Fragment fragment)
	{
		stacks.get(currentTabIndex).push(fragment);
	}
	
	public Fragment popFragment()
	{
		if(currentTabIndex == -1 || stacks.get(currentTabIndex).size() <= 1)
			return null;
		
		return stacks.get(currentTabIndex).pop();
	}
	
	public Fragment getCurrentVisibleFragment()
	{
		return currentTabIndex >= 0 ? stacks.get(currentTabIndex).peek() : null;
	}
}
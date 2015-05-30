package com.inomma.kandu.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FormListCategory {

	private String name;
	
	private List<FormListItem> formListItems = new ArrayList<FormListItem>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FormListItem> getFormListItems() {
		return formListItems;
	}

	public void setFormListItems(List<FormListItem> formListItems) {
		this.formListItems = formListItems;
	}
	
	
	
	public void addFormListItem(FormListItem item){
		this.formListItems.add(item);
	}
	
	public void sortItems(){
		Collections.sort(formListItems);
	}
}

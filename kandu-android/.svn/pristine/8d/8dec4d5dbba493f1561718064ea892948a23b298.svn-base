package com.inomma.kandu.model;

public class FormListItem implements Comparable<FormListItem>{

	private UserForm userForm;
	private boolean isEditItem;

	public FormListItem(UserForm userForm, boolean isEditItem) {
		super();
		this.userForm = userForm;
		this.isEditItem = isEditItem;
	}

	public UserForm getUserForm() {
		return userForm;
	}

	public void setUserForm(UserForm userForm) {
		this.userForm = userForm;
	}

	public boolean isEditItem() {
		return isEditItem;
	}

	public void setEditItem(boolean isEditItem) {
		this.isEditItem = isEditItem;
	}

	@Override
	public int compareTo(FormListItem another) {
		return this.userForm.getVisibleName().compareTo(another.userForm.getVisibleName());
	}
	
	

}

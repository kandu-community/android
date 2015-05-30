package com.inomma.kandu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserForm implements Serializable,Comparable<UserForm> {
	
	private static final long serialVersionUID = -5014607772196175837L;
	private String url;
	private Form form;
	private String visibleName;
	private String key;
	private List<UserForm> inlineForms = new ArrayList<UserForm>();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public String getVisibleName() {
		return visibleName;
	}

	public void setVisibleName(String visibleName) {
		this.visibleName = visibleName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public FormItem getGpsItem(){
		for(FormItem item:form.getFormItems()){
			if(item.getFormItemType()==FormItemType.GPS){
				return item;
			}
		}
		return null;
	}

	public List<UserForm> getInlineForms() {
		return inlineForms;
	}

	public void addInlineForm(UserForm inlineForm) {
		this.inlineForms.add(inlineForm);
	}

	@Override
	public int compareTo(UserForm another) {
		return this.getForm().getIndex()>another.getForm().getIndex()?1:-1;
	}
	
	public FormItem getFormItemByKey(String key){
		for(FormItem formItem: form.getFormItems()){
			if(formItem.getKey().equals(key)){
				return formItem;
			}
		}
		for(FormItem formItem: form.getFormItems()){
			if(key.equals(formItem.getLinkTo())){
				return formItem;
			}
		}
		return null;
	}
}

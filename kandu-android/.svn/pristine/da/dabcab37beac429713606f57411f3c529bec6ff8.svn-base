package com.inomma.kandu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFormsHolder {

	private static UserFormsHolder instance;
	private List<UserForm> userForms;

	public static UserFormsHolder getInstance() {
		return instance;
	}

	public static void newInstance(Config config, List<UserForm> userForms) {
		instance = new UserFormsHolder();
		instance.userForms = new ArrayList<UserForm>(userForms); 
		List<UserForm> inlineForms = new ArrayList<UserForm>(); 
		for (UserForm userForm : instance.userForms) {
			Form form = config.getForms().get(userForm.getKey());
			if(form!=null){
				userForm.setForm(config.getForms().get(userForm.getKey()));
				for(Form inline: form.getInlineForms()){
					UserForm inlineUserForm = instance.getUserFormWithKey(inline.getKey());
					inlineUserForm.setForm(inline);
					userForm.addInlineForm(inlineUserForm);
					inlineForms.add(inlineUserForm);
				}
			}
		}
		instance.userForms.removeAll(inlineForms);
		List<UserForm> toDelete = new ArrayList<UserForm>();
		for(UserForm userForm: instance.userForms){
			if(userForm.getForm()==null){
				toDelete.add(userForm);
			}
		}
		instance.userForms.removeAll(toDelete);
		Collections.sort(instance.userForms);
		System.out.println("asd");
	}
	
	public UserForm getUserFormWithKey(String key){
		for(UserForm userForm:userForms){
			if(userForm.getKey().equals(key)){
				return userForm;
			}
			else for(UserForm inline:userForm.getInlineForms()){
				if(inline.getKey().equals(key)){
					return inline;
				}
			}
		}
		return null;
	}

	public List<UserForm> getUserForms() {
		return userForms;
	}
	public List<UserForm> getUserMainForms() {
		List<UserForm> mainForms = new ArrayList<UserForm>();
		for(UserForm userForm:userForms){
			if(userForm.getForm()!=null){
				mainForms.add(userForm);
			}
		}
		return mainForms;
	}
	public void setUserForms(List<UserForm> userForms) {
		this.userForms = userForms;
	}

	public static void setInstance(UserFormsHolder instance) {
		UserFormsHolder.instance = instance;
	}

	private void fillDate() {
	
	}
	
	public List<UserForm> getCacheForms(){
		List<UserForm> userForms = new ArrayList<UserForm>();
		for(UserForm form: this.userForms){
			if(form.getForm().isCacheSubmissionsOffline()){
				userForms.add(form);
			}
		}
		return userForms;
	}

}

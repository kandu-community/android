package com.inomma.kandu.ui.adapters;

import java.security.acl.Group;
import java.util.List;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.inomma.kandu.R;
import com.inomma.kandu.model.FormListCategory;
import com.inomma.kandu.model.FormListItem;
import com.inomma.kandu.ui.views.FormItemView;

public class FormListExpandableAdapter extends BaseExpandableListAdapter {

	  private final List<FormListCategory> categories;
	  public LayoutInflater inflater;
	  public Activity activity;

	public FormListExpandableAdapter(Activity act, List<FormListCategory> categories) {
	    activity = act;
	    this.categories =categories;
	    inflater = act.getLayoutInflater();
	  }

	  @Override
	  public Object getChild(int groupPosition, int childPosition) {
	    return categories.get(groupPosition).getFormListItems().get(childPosition);
	  }

	  @Override
	  public long getChildId(int groupPosition, int childPosition) {
	    return 0;
	  }

	  @Override
	  public View getChildView(int groupPosition, final int childPosition,
	      boolean isLastChild, View convertView, ViewGroup parent) {
	    final FormListItem children = (FormListItem) getChild(groupPosition, childPosition);
	    TextView text = null;
	    if (convertView == null) {
	      convertView = inflater.inflate(R.layout.form_list_item, null);
	    }
	    text = (TextView) convertView.findViewById(R.id.form_name);
	    String title = "";
	    if(children.isEditItem()){
	    	title+="Edit ";
		    text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_edit, 0, 0, 0);
	    }
	    else{
		    text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_new, 0, 0, 0);

	    }
	    text.setText(title+children.getUserForm().getVisibleName());
	   
	    return convertView;
	  }

	  @Override
	  public int getChildrenCount(int groupPosition) {
	    return categories.get(groupPosition).getFormListItems().size();
	  }

	  @Override
	  public FormListCategory getGroup(int groupPosition) {
	    return categories.get(groupPosition);
	  }

	  @Override
	  public int getGroupCount() {
	    return categories.size();
	  }

	  @Override
	  public void onGroupCollapsed(int groupPosition) {
	    super.onGroupCollapsed(groupPosition);
	  }

	  @Override
	  public void onGroupExpanded(int groupPosition) {
	    super.onGroupExpanded(groupPosition);
	  }

	  @Override
	  public long getGroupId(int groupPosition) {
	    return 0;
	  }

	  @Override
	  public View getGroupView(int groupPosition, boolean isExpanded,
	      View convertView, ViewGroup parent) {
	    if (convertView == null) {
	      convertView = inflater.inflate(R.layout.form_category_list_item, null);
	    }
	    FormListCategory category = (FormListCategory) getGroup(groupPosition);
	    ((CheckedTextView) convertView).setText(category.getName());
	    ((CheckedTextView) convertView).setChecked(isExpanded);
	    return convertView;
	  }

	  @Override
	  public boolean hasStableIds() {
	    return false;
	  }

	  @Override
	  public boolean isChildSelectable(int groupPosition, int childPosition) {
	    return true;
	  }
	} 
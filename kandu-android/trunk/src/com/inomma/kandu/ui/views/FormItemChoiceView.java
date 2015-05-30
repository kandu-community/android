package com.inomma.kandu.ui.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inomma.kandu.Utils;
import com.inomma.kandu.model.FormItem;

public abstract class FormItemChoiceView extends FormItemView {

	protected Map<String, List<View>> visibilityMap = new HashMap<String, List<View>>();

	public FormItemChoiceView(Context context) {
		super(context);
	}

	public FormItemChoiceView(Context context, FormItem item) {
		super(context, item);
	}

	public FormItemChoiceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FormItemChoiceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void addVisibility(String choice, View view) {
		List<View> views = visibilityMap.get(choice);
		if (views == null) {
			views = new ArrayList<View>();
			visibilityMap.put(choice, views);
		}
		views.add(view);
	}

	@Override
	protected void fillContent(Context context) {
		super.fillContent(context);
		
		label = new TextView(context);
		label.setTextSize(12);
		label.setText(item.getHint());
		label.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));
		addView(label);
	}
	protected void onChoiceChanged(String... choices) {
		if (choices.length > 0)
			showCheckMark();
		Set<String> selectedChoices = new HashSet<String>(
				Arrays.asList(choices));
		Set<String> allChoices = visibilityMap.keySet();
		for (String choice : allChoices) {
			List<View> views = visibilityMap.get(choice);
			if (views != null) {
				boolean makeVisible = selectedChoices.contains(choice);
				for (View view : views) {
					if (makeVisible) {
						view.setVisibility(View.VISIBLE);
					} else {
						view.setVisibility(View.GONE);
					}
				}
			}
		}
		for (String choice : choices) {
			List<View> views = visibilityMap.get(choice);
			if (views != null) {

			}
		}
	}

	protected int getChoiceIntex(String choice) {
		int i = 0;
		for (String toCheck : item.getChoices()) {
			if (Utils.keyFromName(toCheck).equals(choice)) {
				return i;
			}
			i++;
		}
		return 0;
	}

}

package com.inomma.kandu.ui.views;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.inomma.ui.views.VerticalAspectRatioImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class IconsView extends LinearLayout {
	public IconsView(Context context) {
		super(context);
	}

	public IconsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public IconsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	
	public void setIcons(List<String> icons){
		setOrientation(HORIZONTAL);
		for(String icon:icons){
			VerticalAspectRatioImageView imageView = new VerticalAspectRatioImageView(getContext());
			imageView.setLayoutParams(new LayoutParams(150,LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ScaleType.CENTER_CROP);
			ImageLoader.getInstance().displayImage(icon, imageView);
			addView(imageView);
		}
	}
}

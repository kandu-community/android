package com.inomma.ui;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {
	
	private int diffWidth;
	private int diffHeight;
	private int startWidth;
	private int startHeight;
	private View v;
	private LayoutParams layoutParams;
	
	public ResizeAnimation(View v, int newWidth, int newHeight)
	{
		this.v = v;
		setFillAfter(true);
		setInterpolator(new AccelerateInterpolator());
		
		this.layoutParams = v.getLayoutParams();
		this.startWidth = layoutParams.width;
		this.startHeight = layoutParams.height;
		this.diffWidth = newWidth - startWidth;
		this.diffHeight = newHeight - startHeight;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		layoutParams.width = (int) (startWidth + interpolatedTime * (diffWidth));
		layoutParams.height = (int) (startHeight + interpolatedTime * (diffHeight));
		v.requestLayout();
	}
}

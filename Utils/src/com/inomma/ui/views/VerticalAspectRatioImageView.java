package com.inomma.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class VerticalAspectRatioImageView extends ImageView {

	public VerticalAspectRatioImageView(Context context) {
		super(context);
	}

	public VerticalAspectRatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VerticalAspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable drawable = getDrawable();
		if (drawable == null) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = height * drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
		setMeasuredDimension(width, height);
	}
}

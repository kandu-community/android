package com.inomma.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HorizontalAspectRatioImageView extends ImageView {

	public HorizontalAspectRatioImageView(Context context) {
		super(context);
	}

	public HorizontalAspectRatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalAspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable drawable = getDrawable();
		if (drawable == null) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = width * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
		setMeasuredDimension(width, height);
	}
}

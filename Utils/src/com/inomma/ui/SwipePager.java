package com.inomma.ui;

import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class SwipePager {

	private ViewGroup container;
	private int currentFrameIndex;
	private int animationDuration;
	private OnFrameChangedListener onFrameChangedListener;
	
	public SwipePager(final ViewGroup container, OnFrameChangedListener onFrameChangedListener) {
		this(container, 0, onFrameChangedListener);
	}

	public SwipePager(final ViewGroup container, int firstShowIndex, OnFrameChangedListener onFrameChangedListener) {
		this.container = container;
		this.onFrameChangedListener = onFrameChangedListener;
		animationDuration = 200;
		setSwipeEnabled(true);
		
		if(firstShowIndex < 0)
			firstShowIndex = 0;
		else if (firstShowIndex >= container.getChildCount())
			firstShowIndex = container.getChildCount() -1;
		
		Log.e("SwipePager", "First show index: " + firstShowIndex);
		
		for(int i = 0; i < container.getChildCount(); i++)
		{
			
			container.getChildAt(i).setVisibility(i == firstShowIndex ? View.VISIBLE : View.GONE);
		}
		
		container.getChildAt(0).setVisibility(View.VISIBLE);
		
		currentFrameIndex = firstShowIndex;
		if(onFrameChangedListener != null)
			onFrameChangedListener.onFrameChanged(firstShowIndex);
	}
	
	public boolean hasNext()
	{
		return currentFrameIndex < container.getChildCount() - 1;
	}
	
	public boolean hasPrev()
	{
		return currentFrameIndex > 0;
	}
	
	public boolean showNext()
	{
		return jumpTo(1);
	}
	
	public boolean showPrev()
	{
		return jumpTo(-1);
	}
	
	public boolean jumpTo(int diff)
	{
		return showFrame(currentFrameIndex + diff);
	}
	
	public boolean showFrame(int newFrameIndex) {
		
		if(newFrameIndex <0 || newFrameIndex >= container.getChildCount() || newFrameIndex == currentFrameIndex)
			return false;
		
		int direction = newFrameIndex > currentFrameIndex ? 1 : -1;
		final View current = container.getChildAt(currentFrameIndex);

		currentFrameIndex = newFrameIndex;
		final View coming = container.getChildAt(currentFrameIndex);
		coming.setVisibility(View.VISIBLE);

		Animation currentAnim = new TranslateAnimation(0, -direction * container.getMeasuredWidth(), 0, 0);
		Animation comingAnim = new TranslateAnimation(direction * container.getMeasuredWidth(), 0, 0, 0);

		currentAnim.setAnimationListener(new OnAnimationEndListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				current.setVisibility(View.GONE);
			}
		});
		currentAnim.setDuration(animationDuration);
		comingAnim.setDuration(animationDuration);
		current.startAnimation(currentAnim);
		coming.startAnimation(comingAnim);
		if(onFrameChangedListener != null)
			onFrameChangedListener.onFrameChanged(currentFrameIndex);
		return true;
	}

	public int getCurrentFrameIndex() {
		return currentFrameIndex;
	}

	public void setAnimationDuration(int animationDuration) {
		this.animationDuration = animationDuration;
	}

	public void setSwipeEnabled(boolean swipeEnabled) {
		if(swipeEnabled)
		{
			final GestureDetectorCompat detector = new GestureDetectorCompat(container.getContext(), new SimpleOnGestureListener() {

				@Override
				public boolean onDown(MotionEvent e) {
					return true;
				}

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
					if (Math.abs(velocityX) > 800) {

						if (velocityX > 0 && currentFrameIndex > 0)
							showPrev();
						else if (velocityX < 0 && currentFrameIndex < container.getChildCount())
							showNext();
					}

					return true;
				}
			});
			
			container.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					detector.onTouchEvent(event);
					return true;
				}
			});
		} else
		{
			container.setOnTouchListener(null);
		}
	}

	private abstract class OnAnimationEndListener implements AnimationListener {
		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}
	
	public interface OnFrameChangedListener
	{
		public void onFrameChanged(int index);
	}
}
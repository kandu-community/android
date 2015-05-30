package com.inomma.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.inomma.utils.R;

public class CustomFontButton extends Button {
	
	private String fontName;

	public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomFont, 0, 0);
        fontName = attrsArray.getString(R.styleable.CustomFont_font);
    }

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomFont, 0, 0);
        fontName = attrsArray.getString(R.styleable.CustomFont_font);
    }

    public CustomFontButton(Context context) {
        super(context);
        fontName = null;
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
    	if(fontName == null)
    		fontName = "helvetica";
    	Typeface normal = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName + ".ttf");
    	Typeface bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName + "_bold.ttf");
    	if (style == Typeface.BOLD) {
            super.setTypeface(bold);
        } else {
            super.setTypeface(normal);
        }
    }
}

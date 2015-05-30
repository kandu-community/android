package com.inomma.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.inomma.utils.R;

public class CustomFontTextView extends TextView {
	
	private String fontName;
	private Integer style;

	public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomFont, 0, 0);
        fontName = attrsArray.getString(R.styleable.CustomFont_font);
        refreshTypeface();
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomFont, 0, 0);
        fontName = attrsArray.getString(R.styleable.CustomFont_font);
        refreshTypeface();
    }

    public CustomFontTextView(Context context) {
        super(context);
        fontName = null;
        refreshTypeface();
    }
    
    @Override
    public void setTypeface(Typeface tf) {
    	super.setTypeface(tf);
    	style = null;
    }
    
    @Override
    public void setTypeface(Typeface tf, int style) {
    	super.setTypeface(tf, style);
    	this.style = style;
    }
    
    public void refreshTypeface()
    {
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

package com.snail.french.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

public class FullSizeListView extends ListView {

	public FullSizeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	public FullSizeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	public FullSizeListView(Context context) {
		super(context);
		setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

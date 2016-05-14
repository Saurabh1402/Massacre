package com.massacre.massacre;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by saurabh on 22/4/16.
 */
public class SquaredRelativeLayout extends RelativeLayout {


    public SquaredRelativeLayout(Context context) {
        super(context);
    }

    public SquaredRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaredRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquaredRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);

    }
}

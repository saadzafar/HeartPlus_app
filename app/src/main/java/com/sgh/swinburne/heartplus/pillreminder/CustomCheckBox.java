package com.sgh.swinburne.heartplus.pillreminder;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.sgh.swinburne.heartplus.R;

/**
 * Created by faiza on 4/11/2016.
 */
public class CustomCheckBox extends CheckBox {

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean t) {
        if (t) {
            // checkbox_background is blue
            this.setBackgroundResource(R.drawable.checkbox_background);
            this.setTextColor(Color.WHITE);
        } else {
            this.setBackgroundColor(Color.TRANSPARENT);
            this.setTextColor(Color.WHITE);
        }
        super.setChecked(t);
    }
}
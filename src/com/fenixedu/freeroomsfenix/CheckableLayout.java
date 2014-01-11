package com.fenixedu.freeroomsfenix;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLayout extends LinearLayout implements Checkable {

	public CheckableLayout(Context context) {
		super(context);
	}

	public CheckableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean isChecked() {
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child instanceof Checkable) {
				Checkable checkableChild = (Checkable) child;
				if (checkableChild.isChecked()) {
					return true;
				}
			}
		}
		return false;
	}

	public void setChecked(boolean checked) {
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child instanceof Checkable) {
				Checkable checkableChild = (Checkable) child;
				checkableChild.setChecked(checked);
			}
		}

	}

	public void toggle() {
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child instanceof Checkable) {
				Checkable checkableChild = (Checkable) child;
				checkableChild.toggle();
			}
		}
	}

}

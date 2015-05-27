package com.aidimedia.ui.view;

import com.aidimedia.ui.view.util.ScaleAnimEffect;
import com.example.overridelinearlayout.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class InnerLinearLayout extends LinearLayout implements
		OnFocusChangeListener, OnClickListener {

	View view;
	
	ScaleAnimEffect animEffect;

	public InnerLinearLayout(Context context,ScaleAnimEffect animEffect) {
		super(context);
		Button btn1 = new Button(context);
		btn1.setWidth(50);
		btn1.setHeight(50);
		btn1.setTextColor(Color.WHITE);
		btn1.setText("Button1");

		Button btn2 = new Button(context);
		btn2.setWidth(50);
		btn2.setHeight(50);
		btn2.setTextColor(Color.WHITE);
		btn2.setText("Button2");
		
		this.animEffect=animEffect;

		setBackgroundColor(Color.GRAY);

		addView(btn1);
		addView(btn2);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			showOnFocusAnimation();
			//Toast.makeText(mContext, "onFocusChange-----" + "获得焦点", 500).show();
		} else {
			showLooseFocusAinimation();
			//Toast.makeText(mContext, "onFocusChange-----" + "焦点移除", 500).show();
		}

	}
	
	private void showOnFocusAnimation() {
		this.bringToFront();
		this.animEffect.setAttributs(1.0F, 1.105F, 1.0F, 1.105F, 100L);
		Animation localAnimation = this.animEffect.createAnimation();
		localAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// shadowBackgrounds[paramInt].startAnimation(animEffect.alphaAnimation(0.0F,
				// 1.0F, 150L, 0L));
				// shadowBackgrounds[paramInt].setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}
		});
		this.startAnimation(localAnimation);
	}

	private void showLooseFocusAinimation() {
		this.animEffect.setAttributs(1.105F, 1.0F, 1.105F, 1.0F, 100L);
		this.startAnimation(this.animEffect.createAnimation());
	}

}

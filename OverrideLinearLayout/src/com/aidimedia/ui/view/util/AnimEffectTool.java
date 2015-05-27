package com.aidimedia.ui.view.util;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * heaven
 *2013年12月25日
 * 
 */
public class AnimEffectTool {
	private long duration;
	private float fromAlpha;
	private float fromXScale;
	private float fromYScale;
	private float toAlpha;
	private float toXScale;
	private float toYScale;
	
	public Animation alphaAnimation(float paramFloat1, float paramFloat2, long paramLong1, long paramLong2){
		AlphaAnimation alphaAnimation = new AlphaAnimation(paramLong1, paramLong2);
		alphaAnimation.setDuration(paramLong1);
		alphaAnimation.setStartOffset(paramLong2);
		alphaAnimation.setInterpolator(new AccelerateInterpolator());
		return alphaAnimation;
	}
	
	public Animation createAnimation(){
		ScaleAnimation scaleAnimation = new ScaleAnimation(this.fromXScale, this.toXScale, this.fromYScale, this.toYScale, 1, 0.5F, 1, 0.5F);
		scaleAnimation.setFillAfter(true);
		scaleAnimation.setInterpolator(new AccelerateInterpolator());
		scaleAnimation.setDuration(this.duration);
		return scaleAnimation;
		
	}
	
	public void setAttributs(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong) {
		this.fromXScale = paramFloat1;
		this.fromYScale = paramFloat3;
		this.toXScale = paramFloat2;
		this.toYScale = paramFloat4;
		this.duration = paramLong;
	}

}

package com.aidimedia.ui.view;

import com.aidimedia.ui.view.util.ScaleAnimEffect;
import com.aidimedia.ui.view.util.StringUtil;
import com.example.overridelinearlayout.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ItemImageView extends ImageView implements OnFocusChangeListener,
		OnClickListener {

	public static final int MSG_START_LOAD_SNAPSHOT = 1000;
	public static final int MSG_LOADING_SNAPSHOT = 1001;
	public static final int MSG_FINISH_LOAD_SNAPSHOT = 1002;

	private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG
			| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
			| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
			| Canvas.CLIP_TO_LAYER_SAVE_FLAG;

	private static int mCornerRound;
	private static Drawable mTitleBG;
	private Context mContext;
	private Drawable mDrawableSelected;

	private static int mTextPadding;
	private static int mTextY;
	private static int mTextSize;
	private static int mTitlePadding;
	private static int mTitlePaddingBottom;

	private FastlinkHandler mHandler;
	public Link mLink;
	private String mText;
	private Paint mPaint;
	private Bitmap mImage;
	private int mIndex = 0;
	private int mItemWidth = -1;
	private int mItemHeight = -1;
	private int mItemInnerH = -1;
	private int mItemInnerW = -1;
	public boolean isLoading = false;

	private int mLastX;
	private int mLastY;

	private ScaleAnimEffect animEffect;

	public ItemImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ItemImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ItemImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setIndex(int index) {
		mIndex = index;
	}

	public ItemImageView(Context context, Link mLink, int mItemWidth,
			int mItemHeight, ScaleAnimEffect animEffect) {
		this(context, null);
		setFocusable(true);
		setOnClickListener(this);
		setOnFocusChangeListener(this);
		// setScaleType(ScaleType.FIT_CENTER);
		this.animEffect = animEffect;

		mHandler = new FastlinkHandler();

		this.mContext = context;
		this.mLink = mLink;
		this.mItemWidth = mItemWidth;
		this.mItemHeight = mItemHeight;

		mItemInnerH = mContext.getResources().getDimensionPixelSize(
				R.dimen.fast_link_item_inner_height);
		mItemInnerW = mItemInnerH;
		mCornerRound = mContext.getResources().getDimensionPixelSize(
				R.dimen.fast_link_item_snapshot_corner);
		mTitleBG = mContext.getResources().getDrawable(R.drawable.dot_view_bg);
		// mTitleBG=new ColorDrawable(Color.GRAY);

		mPaint = new Paint();
		mPaint.setTextSize(mTextSize);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.WHITE);

		setLink(mLink);
	}

	public void setLink(Link link) {
		if (link == null) {
			mLink = new Link("哈哈", "hyc");
			setTitle("测试");
			setEmptyImage();
			return;
		}

		mLink = link;

		setTitle(mLink.getTitle());
		if (link.getObj() != null) {
			setImageBitmap((Bitmap) link.getObj());
		} else {
			if (isLoading) {
				setLoadingBackgound();
				mHandler.sendEmptyMessage(MSG_LOADING_SNAPSHOT);
			} else {
				if (TextUtils.isEmpty(link.getUrl())) {
					setEmptyImage();
				} else {
					setDefaultImage();
				}
			}
		}

	}

	public Bitmap getImage() {
		return mImage;
	}

	private void setDefaultImage() {
		Bitmap defaultSnapshot = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		// super.setImageBitmap(defaultSnapshot);
		// setDefaultBackground();
		Drawable bg = generateIconDrawable(defaultSnapshot);
		// setBackgroundDrawable(bg);
		setImageDrawable(bg);

		setBackgroundResource(R.drawable.activity_bg);
	}

	private Drawable generateIconDrawable(Bitmap icon) {
		int width = getWidth();
		int height = getHeight();
		Drawable[] drawables = new Drawable[2];
		drawables[0] = new BitmapDrawable(icon);

		Drawable selector = getResources().getDrawable(
				R.drawable.fast_link_item_selector);
		selector.setBounds(0, 0, width, height);
		drawables[1] = selector;

		LayerDrawable layerDrawable = new LayerDrawable(drawables);
		layerDrawable.setBounds(0, 0, width, height);
		return layerDrawable;
	}

	private void setLoadingBackgound() {
		setBackgroundResource(R.drawable.fast_link_item_bg_loading);
	}

	private void setEmptyImage() {
		setImageBitmap(null);
		setEmptyBackground();
	}

	private void setEmptyBackground() {
		setBackgroundResource(R.drawable.fast_link_item_empty_bg);
	}

	public void setTitle(String title) {
		if (mLink != null) {
			mLink.setTitle(title);
			if (StringUtil.isEmpty(title)) {
				mText = null;
			} else {
				mText = StringUtil.textCutoff(title, mPaint, mItemWidth
						- mTitlePadding * 2 - mTextPadding);
			}
		}

		mText = "测试";
	}

	private class FastlinkHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_START_LOAD_SNAPSHOT:
				Bundle data = msg.getData();
				String url = data.getString("url");

				if (TextUtils.isEmpty(url) || mLink == null) {
					return;
				}

				setTitle(data.getString("title"));
				mLink.setUrl(url);

				if (getDrawable() != null) {
					setImageDrawable(null);
				}

				setLoadingBackgound();

				mHandler.sendEmptyMessage(MSG_LOADING_SNAPSHOT);

				// ��ʼ���ؿ���
				// Snapshot snapshot = new Snapshot(mLink.getName(),
				// mLink.getUrl(), getIndex());
				// AppEngine.getInstance().getSnapshotManager().obtainSnapshot(snapshot,
				// FastLinkItemView.this);

				isLoading = true;

				break;
			case MSG_LOADING_SNAPSHOT:

				startAnimation();

				break;
			case MSG_FINISH_LOAD_SNAPSHOT:

				stopAnimation();

				if (mLink.getObj() instanceof Bitmap) {
					setImageBitmap((Bitmap) mLink.getObj());
				} else {
					// setErrorImage();
					setDefaultImage();
				}

				isLoading = false;
				break;
			default:
				break;
			}
		}
	}

	private void startAnimation() {
		Drawable background = getBackground();
		if (background instanceof LayerDrawable) {
			LayerDrawable drawable = (LayerDrawable) background;
			Drawable animDrawable = drawable
					.findDrawableByLayerId(R.id.fast_link_item_loading_level1);
			if (animDrawable instanceof AnimationDrawable) {
				AnimationDrawable anim = (AnimationDrawable) animDrawable;
				anim.setCallback(ItemImageView.this);
				anim.stop();
				anim.start();
			}
		}
	}

	private void stopAnimation() {
		Drawable backGrd = getBackground();
		if (backGrd instanceof LayerDrawable) {
			LayerDrawable drawable = (LayerDrawable) backGrd;
			AnimationDrawable animDrawable = (AnimationDrawable) drawable
					.findDrawableByLayerId(R.id.fast_link_item_loading_level1);
			if (animDrawable != null) {
				animDrawable.stop();
				animDrawable.setCallback(null);
			}
		}
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		mImage = bm;
		if (bm != null) {
			Bitmap temp = bm;

			temp = getRoundedCornerBitmap(bm);

			Drawable image = generateIconDrawable(temp);
			// setBackgroundDrawable(image);

			super.setImageDrawable(image);
			// super.setImageDrawable(null);
			mLink.setObj(temp);

			if (temp != bm) {
				recyleBitmap(bm);
			}
		}

	}

	private void recyleBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
			bitmap = null;
		}
	}

	private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}

		final int dstWidth = mItemWidth - getPaddingLeft() - getPaddingRight();
		final int dstHeight = mItemHeight - getPaddingTop()
				- getPaddingBottom();

		Bitmap dstBitmap = null;

		try {
			dstBitmap = Bitmap.createBitmap(dstWidth, dstHeight,
					Config.ARGB_8888);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}

		Canvas canvas = new Canvas(dstBitmap);
		final int color = 0xff424242;
		final Paint paint = new Paint();

		final Rect srcRect = new Rect(0, 0, mItemInnerW, mItemInnerH);

		final int left = (dstWidth - mItemInnerW) / 2;
		final int top = (dstHeight - mItemInnerH) / 2;
		final RectF dstRect = new RectF(left, top, dstWidth - left, dstHeight
				- top);
		final float roundPx = mCornerRound;

		paint.setAntiAlias(true);
		paint.setDither(true);

		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);
		int save = canvas.saveLayer(dstRect, null, LAYER_FLAGS);
		canvas.drawRoundRect(dstRect, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, srcRect, dstRect, paint);

		canvas.restoreToCount(save);

		return dstBitmap;
	}

	public void updateSize(int width, int height) {
		mItemWidth = width;
		mItemHeight = height;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (!StringUtil.isEmpty(mLink.getTitle()) && !isLoading
				&& mText != null) {
			int y = getHeight();

			// 绘制标题背景
			canvas.save();

			final int padding = mTitlePadding;

			int w = getWidth() - padding * 2;

			int h = mTitleBG.getIntrinsicHeight();

			canvas.translate(padding, y - h - mTitlePaddingBottom);
			mTitleBG.setBounds(0, 0, w, h);
			mTitleBG.draw(canvas);
			canvas.restore();

			float fontSize = mPaint.measureText(mText);
			mText = "sssssssssssssssssssss";

			canvas.drawText(mText, (getWidth() - fontSize) / 2, y - mTextY,
					mPaint);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		stopAnimation();
		startAnimation();

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		// stopAnimation();
		// startAnimation();
		// this.bringToFront();
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

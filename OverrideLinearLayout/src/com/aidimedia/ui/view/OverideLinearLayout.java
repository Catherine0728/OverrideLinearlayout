package com.aidimedia.ui.view;

import com.aidimedia.ui.view.util.ScaleAnimEffect;
import com.example.overridelinearlayout.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.AbsoluteLayout.LayoutParams;

/**
 * @author heaven
 * 
 */
public class OverideLinearLayout extends LinearLayout {

	private static final int ITEM_COUNT = 39;
	private int mDefaultLineSpace = 10;
	private int mDefaultItemHeight = 300;
	private int mDefaultItemWidth = 600;

	private ItemImageView[] mItems = new ItemImageView[ITEM_COUNT];

	private ScaleAnimEffect animEffect;

	public OverideLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public OverideLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public OverideLinearLayout(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		setFocusable(true);
		setClickable(true);
		// setGravity(Gravity.CENTER_VERTICAL);
		setOrientation(LinearLayout.HORIZONTAL);

		Resources res = context.getResources();
		this.animEffect = new ScaleAnimEffect();
		mDefaultItemWidth = res
				.getDimensionPixelSize(R.dimen.fast_link_item_width);
		mDefaultItemHeight = res
				.getDimensionPixelSize(R.dimen.fast_link_item_height);
		mDefaultLineSpace = res
				.getDimensionPixelSize(R.dimen.fast_link_item_line_space);

		createDefaultItems(context);
	}

	private void createDefaultItems(Context context) {
		int itemWidth = mDefaultItemWidth;
		int itemHeight = mDefaultItemHeight;

		for (int i = 0; i < ITEM_COUNT; i++) {
			Link link = new Link("测试heaven", "http://www.baidu.com");
			// ItemImageView item = new ItemImageView(context, link, itemWidth,
			// itemHeight, animEffect);
			// if(i == 0){
			// item.requestFocus();
			// }
			InnerLinearLayout lin = new InnerLinearLayout(context, animEffect);
			View view = LayoutInflater.from(context).inflate(
					R.layout.view_item, null);
			LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
			lin.setId(i + 1);
			// lin.setIndex(i);
			addView(lin, lp);
			// addView(view, lp);

			// mItems[i] = item;
		}
	}

	private int getColumns() {
		int colomns = ITEM_COUNT / 2;
		if (ITEM_COUNT % 2 == 0) {
			colomns = ITEM_COUNT / 2;
		} else if (ITEM_COUNT % 2 != 0) {
			colomns = ITEM_COUNT / 2 + 1;
		}

		return colomns;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		final int columns = getColumns();
		final int parentWidth = r - l;
		final int parentHight = b - t;
		final int itemCount = getChildCount();

		final int itemsPerColumn = 2;
		final int lineSpace = mDefaultLineSpace;
		int itemWidth = mDefaultItemWidth;
		int itemHight = mDefaultItemHeight;
		int spaceX = 0;
		int spaceY = 0;
		int startX = 0;
		int startY = 0;
		int x = 0;
		int y = 0;

		if (true/* 后期添加空间判断使用 */) {
			spaceX = (parentWidth - columns * itemWidth) / (columns + 1);
			x = startX = mDefaultLineSpace;
			spaceY = mDefaultLineSpace;
			y = (parentHight - itemHight * itemsPerColumn - spaceY
					* (itemsPerColumn - 1)) / 2;
			startY = y;
		}

		for (int i = 0; i < columns; i++) {
			y = startY;
			for (int j = 0; j < itemsPerColumn; j++) {
				int index = i * itemsPerColumn + j;
				if (index < itemCount) {
					// ItemImageView item = (ItemImageView) getChildAt(index);
					// item.layout(x, y, x + itemWidth, y + itemHight);
					// item.updateSize(itemWidth, itemHight);

					// View view = getChildAt(index);
					// view.layout(x, y, x + itemWidth, y + itemHight);
					// myLinearLayout lin=(myLinearLayout)getChildAt(index);
					// lin.layout(x, y, x + itemWidth, y + itemHight);
					// lin.updateSize(itemWidth, itemHight);
					InnerLinearLayout lin = (InnerLinearLayout) getChildAt(index);
					lin.layout(x, y, x + itemWidth, y + itemHight);
				} else {
					break;
				}
				y += (itemHight + spaceY);
			}
			x += (itemWidth + spaceX);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int measuredHeight = resolveSize(Integer.MAX_VALUE, heightMeasureSpec);

		final int column = getColumns();

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		final int desiredWidth = (mDefaultItemWidth + mDefaultLineSpace)
				* (column) + mDefaultLineSpace + getPaddingLeft()
				+ getPaddingRight();

		setMeasuredDimension(resolveSize(desiredWidth, widthMeasureSpec),
				measuredHeight);
	}

}

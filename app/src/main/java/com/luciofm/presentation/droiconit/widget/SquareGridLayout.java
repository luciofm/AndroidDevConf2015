package com.luciofm.presentation.droiconit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.luciofm.presentation.droiconit.R;


public class SquareGridLayout extends ViewGroup {

	private int mSpacing = 0;
	private int mColumns = 2;

	public SquareGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SquareGridLayout);
		try {
			mSpacing = a.getDimensionPixelSize(
					R.styleable.SquareGridLayout_spacing, 0);
			mColumns = a.getInt(R.styleable.SquareGridLayout_numColumns, 2);
		} finally {
			a.recycle();
		}
		mCurrentHeight = new int[mColumns];
	}

	public SquareGridLayout(Context context) {
		super(context);
	}

	private int[] mCurrentHeight;

	public void setColumnCount(int size) {
		if (size < 1) {
			throw new IllegalArgumentException(
					"Layout must have at least one column");
		}

		mColumns = size;
		mCurrentHeight = new int[mColumns];
		requestLayout();
	}

	public int getColumnCount() {
		return mColumns;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int padw = getPaddingLeft() + getPaddingRight();

		int squareWidth = (widthSize - padw - (mColumns > 1 ? (mColumns + 1)
				* mSpacing : 0))
				/ mColumns;

		int childSpec = MeasureSpec.makeMeasureSpec(squareWidth,
                MeasureSpec.EXACTLY);

		int width = 0;
		int[] height = new int[mColumns];
		for (int i = 0; i < mColumns; i++) {
			height[i] = getPaddingTop();
			mCurrentHeight[i] = 0;
		}

		int currentWidth = getPaddingLeft();
		int currentColumn = 0;

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			measureChild(child, childSpec, childSpec);

			if (currentWidth + child.getMeasuredWidth() + mSpacing > widthSize) {
				for (int j = 0; j < mColumns; j++) {
					height[j] += mCurrentHeight[j] + mSpacing;
					mCurrentHeight[j] = 0;
				}
				width = Math.max(width, currentWidth);
				currentWidth = getPaddingLeft();
			}

			lp.x = currentWidth + mSpacing;
			lp.y = height[currentColumn] + mSpacing;

			currentWidth += child.getMeasuredWidth() + mSpacing;
			mCurrentHeight[currentColumn] = Math.max(
                    mCurrentHeight[currentColumn], child.getMeasuredHeight());
			currentColumn++;
			if (currentColumn >= mColumns)
				currentColumn = 0;
		}

		for (int i = 0; i < mColumns; i++)
			height[i] += mCurrentHeight[i] + mSpacing + getPaddingBottom()
					+ mSpacing;

		width = Math.max(width, currentWidth - mSpacing);

		width += getPaddingRight();

		setMeasuredDimension(resolveSize(width, widthMeasureSpec),
				resolveSize(getMaxHeight(height), heightMeasureSpec));
	}

	private int getMaxHeight(int[] heights) {
		int height = 0;
		for (int i = 0; i < heights.length; i++) {
			if (heights[i] > height)
				height = heights[i];
		}
		return height;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			LayoutParams lp = (LayoutParams) child.getLayoutParams();

			child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y
					+ child.getMeasuredHeight());
		}
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p.width, p.height);
	}

	public static class LayoutParams extends ViewGroup.LayoutParams {

		public int x;
		public int y;

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
	}
}

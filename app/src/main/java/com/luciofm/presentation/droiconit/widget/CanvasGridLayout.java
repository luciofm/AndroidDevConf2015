package com.luciofm.presentation.droiconit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.luciofm.presentation.droiconit.R;

public class CanvasGridLayout extends ViewGroup {

	private int mSpacing = 0;
    private static final int MAX_CHILD = 6;

	public CanvasGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CanvasGridLayout);
		try {
			mSpacing = a.getDimensionPixelSize(
                    R.styleable.CanvasGridLayout_entrySpacing, 0);
		} finally {
			a.recycle();
		}
	}

	public CanvasGridLayout(Context context) {
		super(context);
	}

    public int getSpacing() {
        return mSpacing;
    }

    public void setSpacing(int mSpacing) {
        this.mSpacing = mSpacing;
        invalidate();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() == MAX_CHILD)
            throw new IllegalStateException("Can't add more than 6 views do this ViewGroup");

        super.addView(child, index, params);
    }

    /**
     * We use a little trick here, measuring and actually layout at the same time
     * We save the X,Y position of each child on its LayoutParams...
     *
     * XXX - It could be more generic, but works fine right now...
     */
    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modew = MeasureSpec.getMode(widthMeasureSpec);
        int modeh = MeasureSpec.getMode(heightMeasureSpec);
        if (modew != MeasureSpec.EXACTLY || modeh != MeasureSpec.EXACTLY)
            throw new IllegalArgumentException("width and height must have absolute sizes");

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int padw = getPaddingLeft() + getPaddingRight();
        int padh = getPaddingBottom() + getPaddingTop();

        boolean landscape = widthSize > heightSize;

        int halfSpace = mSpacing > 0 ? mSpacing / 2 : 0;
        int fullHeight = heightSize - padh;
        int fullWidth = widthSize - padw;
        int halfHeight = landscape ? fullHeight : ((heightSize - padh) / 2) - halfSpace;
        int halfWidth = landscape ? ((fullWidth - padw) / 2) - halfSpace : fullWidth;
        int quarterHeight = landscape ? ((heightSize - padh) / 2) - halfSpace : halfHeight;
        int quarterWidth = ((fullWidth - padw) / 2) - halfSpace;
        int eighthHeight = landscape ? quarterHeight : (halfHeight / 2) - halfSpace;
        int eighthWidht = landscape ? (halfWidth / 2) - halfSpace : quarterWidth;

        int viewCount = getChildCount();

        if (viewCount == 1) {
            /* measure for only one child, it's just MATCH parent
             *  -------
             * |       |     ------------
             * |       |    |            |
             * |   1   |    |     1      |
             * |       |    |            |
             * |       |     ------------
             *  -------
             * */
            View child = getChildAt(0);
            int childSpecW = MeasureSpec.makeMeasureSpec(fullWidth,
                    MeasureSpec.EXACTLY);
            int childSpecH = MeasureSpec.makeMeasureSpec(fullHeight,
                    MeasureSpec.EXACTLY);
            measureChild(child, childSpecW, childSpecH);

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.x = padw;
            lp.y = padh;
        } else if (viewCount == 2) {
            /* measure two children, half screen for each one, considering landscape
             *
             *  -------
             * |       |     -----------
             * |   1   |    |     |     |
             * |-------|    |  1  |  2  |
             * |   2   |    |     |     |
             * |       |     -----------
             *  -------
             */
            measureTwoChild(0, halfWidth, halfHeight, padw, padh, landscape);
        } else if (viewCount == 3) {
            /* measure 3 children, two on the first half, and another on the second half
             *
             *  -------
             * |   |   |     -----------
             * | 1 | 2 |    |  1  |     |
             * |-------|    |-----|  3  |
             * |   2   |    |  2  |     |
             * |       |     -----------
             *  -------
             */
            measureTwoChild(0, quarterWidth, quarterHeight, padw, padw, !landscape);

            int childSpecW;
            int childSpecH;
            childSpecW = MeasureSpec.makeMeasureSpec(halfWidth, MeasureSpec.EXACTLY);
            childSpecH = MeasureSpec.makeMeasureSpec(halfHeight, MeasureSpec.EXACTLY);


            measureChild(getChildAt(2), childSpecW, childSpecH);

            LayoutParams lp3 = (LayoutParams) getChildAt(2).getLayoutParams();

            if (landscape) {
                lp3.x = padw + mSpacing + quarterWidth;
                lp3.y = padh;
            } else {
                lp3.x = padw;
                lp3.y = padh + mSpacing + quarterHeight;
            }
        } else if (viewCount == 4) {
            /* measure 4 children, two on the first half, and two on the second half
             *
             *  -------
             * |   |   |     -----------
             * | 1 | 2 |    |  1  |  3  |
             * |-------|    |-----|-----|
             * |   |   |    |  2  |  4  |
             * | 3 | 4 |     -----------
             *  -------
             */
            measureTwoChild(0, quarterWidth, quarterHeight, padw, padw, !landscape);
            if (landscape) {
                measureTwoChild(2, quarterWidth, quarterHeight, padw + mSpacing + quarterWidth,
                                padh,
                                !landscape);
            } else {
                measureTwoChild(2, quarterWidth, quarterHeight, padw,
                                padh + mSpacing + quarterHeight,
                                !landscape);
            }
        } else if (viewCount == 5) {
            /* measure 5 children
             *
             *  -------
             * |   | 2 |     -----------
             * | 1 | 3 |    |  1  |  4  |
             * |-------|    |-----|-----|
             * |   |   |    | 2|3 |  5  |
             * | 4 | 5 |     -----------
             *  -------
             */
            int childSpecW;
            int childSpecH;
            if (landscape) {
                childSpecW = MeasureSpec.makeMeasureSpec(halfWidth, MeasureSpec.EXACTLY);
                childSpecH = MeasureSpec.makeMeasureSpec(quarterWidth, MeasureSpec.EXACTLY);
            } else {
                childSpecW = MeasureSpec.makeMeasureSpec(quarterWidth, MeasureSpec.EXACTLY);
                childSpecH = MeasureSpec.makeMeasureSpec(halfHeight, MeasureSpec.EXACTLY);
            }

            measureChild(getChildAt(0), childSpecW, childSpecH);
            LayoutParams lp = (LayoutParams) getChildAt(0).getLayoutParams();
            lp.x = padw;
            lp.y = padh;

            if (landscape) {
                measureTwoChild(1, eighthWidht, eighthHeight, padw, padh + quarterHeight + mSpacing,
                                landscape);
                measureTwoChild(3, quarterWidth, quarterHeight, padw + mSpacing + quarterWidth,
                                padh,
                                !landscape);
            } else {
                measureTwoChild(1, eighthWidht, eighthHeight, padw + quarterWidth + mSpacing, padh,
                                landscape);
                measureTwoChild(3, quarterWidth, quarterHeight, padw,
                                padh + mSpacing + quarterHeight,
                                !landscape);
            }
        } else if (viewCount == 6) {
            /* measure 6 children
             *
             *  -------
             * |   | 2 |     -----------
             * | 1 | 3 |    |  1  | 4|5 |
             * |-------|    |-----|-----|
             * | 4 |   |    | 2|3 |  6  |
             * | 5 | 6 |     -----------
             *  -------
             */
            int childSpecW;
            int childSpecH;
            if (landscape) {
                childSpecW = MeasureSpec.makeMeasureSpec(halfWidth, MeasureSpec.EXACTLY);
                childSpecH = MeasureSpec.makeMeasureSpec(quarterWidth, MeasureSpec.EXACTLY);
            } else {
                childSpecW = MeasureSpec.makeMeasureSpec(quarterWidth, MeasureSpec.EXACTLY);
                childSpecH = MeasureSpec.makeMeasureSpec(halfHeight, MeasureSpec.EXACTLY);
            }

            measureChild(getChildAt(0), childSpecW, childSpecH);
            LayoutParams lp = (LayoutParams) getChildAt(0).getLayoutParams();
            lp.x = padw;
            lp.y = padh;

            measureChild(getChildAt(5), childSpecW, childSpecH);
            lp = (LayoutParams) getChildAt(5).getLayoutParams();
            if (landscape) {
                lp.x = padw + halfWidth + mSpacing;
                lp.y = padh + quarterHeight + mSpacing;
            } else {
                lp.x = padw + quarterWidth + mSpacing;
                lp.y = padh + halfHeight + mSpacing;
            }

            if (landscape) {
                measureTwoChild(1, eighthWidht, eighthHeight, padw, padh + quarterHeight + mSpacing,
                                landscape);
                measureTwoChild(3, eighthWidht, eighthHeight, padw + halfWidth + mSpacing, padh,
                                landscape);

            } else {
                measureTwoChild(1, eighthWidht, eighthHeight, padw + quarterWidth + mSpacing, padh,
                                landscape);
                measureTwoChild(3, eighthWidht, eighthHeight, padw, padh + halfHeight + mSpacing,
                                landscape);
            }
        }

		setMeasuredDimension(resolveSize(widthSize, widthMeasureSpec),
				resolveSize(heightSize, heightMeasureSpec));
	}

    private void measureTwoChild(int pos, int width, int height, int startX, int startY,
                                 boolean landscape) {
        int childSpecW = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int childSpecH = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        measureChild(getChildAt(pos), childSpecW, childSpecH);
        measureChild(getChildAt(pos + 1), childSpecW, childSpecH);

        LayoutParams lp1 = (LayoutParams) getChildAt(pos).getLayoutParams();
        LayoutParams lp2 = (LayoutParams) getChildAt(pos + 1).getLayoutParams();

        lp1.x = startX;
        lp1.y = startY;

        if (landscape) {
            lp2.x = startX + mSpacing + width;
            lp2.y = startY;
        } else {
            lp2.x = startX;
            lp2.y = startY + mSpacing + height;
        }
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

package com.jwl.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

    private float mHorizontalMargin ;
    private float mVerticalMargin;
    private int mHeightestValue;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mHorizontalMargin = t.getDimensionPixelSize(R.styleable.FlowLayout_horizontal_margin, 20);
        mVerticalMargin = t.getDimensionPixelSize(R.styleable.FlowLayout_vertical_margin, 20);
        t.recycle();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mHorizontalMargin = t.getDimensionPixelSize(R.styleable.FlowLayout_horizontal_margin, 20);
        mVerticalMargin = t.getDimensionPixelSize(R.styleable.FlowLayout_vertical_margin, 20);
        t.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        measureChildren(widthMeasureSpec, heightMeasureSpec);
        getHeightestValue();

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingRight = getPaddingRight();
        getHeightestValue();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (paddingLeft != getPaddingLeft()) {
                if (paddingLeft + child.getMeasuredWidth() > widthSize - paddingRight) {
                    paddingTop += mHeightestValue + mVerticalMargin;
                    paddingLeft = getPaddingLeft();
                }
            }
            paddingLeft += child.getMeasuredWidth() + mHorizontalMargin;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                setMeasuredDimension(widthSize,heightSize);
                break;
            case MeasureSpec.AT_MOST:
                setMeasuredDimension(widthSize,heightSize>paddingTop+mHeightestValue+paddingBottom?paddingTop+mHeightestValue+paddingBottom:heightSize);
                break;
            default:
                setMeasuredDimension(widthMeasureSpec,paddingTop+mHeightestValue+paddingBottom);
                break;
        }

    }


    private void getHeightestValue() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            mHeightestValue = child.getMeasuredHeight() > mHeightestValue ? child.getMeasuredHeight() : mHeightestValue;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (paddingLeft!=getPaddingLeft()) {
                if (paddingLeft+child.getMeasuredWidth()>getWidth()-paddingRight){
                    paddingTop+=mHeightestValue+mVerticalMargin;
                    paddingLeft = getPaddingLeft();
                }
            }
            child.layout(paddingLeft,paddingTop,paddingLeft+child.getMeasuredWidth(),paddingTop+mHeightestValue);
            paddingLeft += child.getMeasuredWidth()+mHorizontalMargin;
        }

    }
}

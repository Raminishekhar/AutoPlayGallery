package com.jekton.autoplaygallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * @author Jekton Luo
 * @version 0.01 6/7/2015.
 */
public class RoundPositionIndicator extends  AutoPlayGallery.PositionIndicator {

    private int selectedColor = 0x4b61d5;
    private int unselectedColor = 0xFFF;
    private float radius = 6f;
    private float margin = 1f;
    private int count;
    private int currentPosition = 0;

    private Paint selectedCirclePaint;
    private Paint unselectedCirclePaint;

    private boolean settingChanged = false;


    public RoundPositionIndicator(Context context) {
        super(context);
        initPaint();
    }

    public RoundPositionIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundPositionIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundPositionIndicator);


        selectedColor = typedArray.getColor(R.styleable.RoundPositionIndicator_my_selected_color, selectedColor);
        unselectedColor = typedArray.getColor(R.styleable.RoundPositionIndicator_my_unselected_color, unselectedColor);
        radius = typedArray.getDimension(R.styleable.RoundPositionIndicator_my_radius, radius);
        margin = typedArray.getDimension(R.styleable.RoundPositionIndicator_my_round_margin, margin);
        count = typedArray.getInt(R.styleable.RoundPositionIndicator_my_count, count);
        currentPosition = typedArray.getInt(R.styleable.RoundPositionIndicator_my_selected, currentPosition);

        typedArray.recycle();
        initPaint();
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public RoundIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void initPaint() {
        selectedCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedCirclePaint.setColor(selectedColor);

        unselectedCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unselectedCirclePaint.setColor(unselectedColor);
    }






    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);

        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                return height;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                return (int) ((radius + margin) * 2);
            default:
                return height;
        }
    }

    private int measureWidth(int widthMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                return width;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                return (int) ((radius + margin) * 2 * count);
            default:
                return width;
        }
    }


    /**
     * Steps:
     * 1. Draw the several circles with {@link #unselectedColor}
     * 2. Draw a circle with {@link #selectedColor} at position {@link #currentPosition}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (settingChanged) {
            initPaint();
            settingChanged = false;
        }

        int pivotX = (int) (radius + margin);
        int pivotY = getMeasuredHeight() / 2;

        canvas.save();
        for (int i = 0; i < count; i++) {
            canvas.drawCircle(pivotX, pivotY, radius, unselectedCirclePaint);
            canvas.translate(pivotX * 2, 0);
        }
//        canvas.translate(pivotX * 2 * (count - 1), 0);

        canvas.restore();
        canvas.drawCircle(pivotX * currentPosition * 2 + pivotX, pivotY, radius, selectedCirclePaint);
    }



    //************************************************
    // Getter and Setters
    //************************************************

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        settingChanged = true;
    }

    public int getUnselectedColor() {
        return unselectedColor;
    }

    public void setUnselectedColor(int unselectedColor) {
        this.unselectedColor = unselectedColor;
        settingChanged = true;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        settingChanged = true;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
        settingChanged = true;
    }

    public int getCount() {
        return count;
    }


    /**
     * {@inheritDoc}
     * @param count total number of the indicator
     */
    @Override
    public void setCount(int count) {
        this.count = count;
        settingChanged = true;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        settingChanged = true;
    }
}

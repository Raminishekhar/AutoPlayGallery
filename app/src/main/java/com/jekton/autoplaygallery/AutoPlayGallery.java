package com.jekton.autoplaygallery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Use {@link ViewPager} to implement an auto played gallery <br />
 *
 * @author Jekton Luo
 * @version 0.01 6/7/2015.
 */
public class AutoPlayGallery extends FrameLayout {

    // used to send message to handler
    private static final int AUTO_PLAY = 1;


    private ViewPager viewPager;
    private GalleryPagerAdapter pagerAdapter;
    private PositionIndicator indicator;
    private Drawable[] drawables;

    private Handler handler;
    private Timer autoPlayTimer;


    public AutoPlayGallery(Context context) {
        super(context);
        init();
    }

    public AutoPlayGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoPlayGallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
//
////    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
////    public AutoPlayGallery(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
////        super(context, attrs, defStyleAttr, defStyleRes);
////    }
//
//
    private void init() {
        final LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_auto_play_gallery, this);


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case AUTO_PLAY:
                        int nextItem = (viewPager.getCurrentItem() + 1) % 4;
                        viewPager.setCurrentItem(nextItem, true);

                        // programmatically call the <code>setCurrentItem()</code> wouldn't trigger
                        // the <code>OnPageChangeListener</code>, must manually set the position of
                        // indicator
                        if (indicator != null) {
                            indicator.setCurrentPosition(nextItem);
                            indicator.invalidate();
                        }
                        return true;
                }
                return false;
            }
        });

    }




    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                stopPlay();
                break;
            case MotionEvent.ACTION_UP:
                startPlay(2000);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }



    public void startPlay(long milliseconds) {
        autoPlayTimer = new Timer();
        autoPlayTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(AUTO_PLAY);
            }
        }, milliseconds, milliseconds);
    }

    public void stopPlay() {
        autoPlayTimer.cancel();
    }




    //*************************************************************
    // Getter and Setters
    //*************************************************************


    public Drawable[] getDrawables() {
        return drawables;
    }

    public void setDrawables(Drawable[] drawables) {
        this.drawables = drawables;

        pagerAdapter = new GalleryPagerAdapter(getContext(), drawables);


        viewPager = (ViewPager) findViewById(R.id.widget_auto_play_gallery_view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_SETTLING:
                        // called when manually scroll the images
                        if (indicator != null) {
                            indicator.setCurrentPosition(viewPager.getCurrentItem());
                            indicator.invalidate();
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        break;
                }
            }
        });
    }

    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }


    public AutoPlayGallery.OnImageClickedListener getOnImageClickedListener() {
        return pagerAdapter.getOnImageClickedListener();
    }

    public void setOnImageClickedListener(AutoPlayGallery.OnImageClickedListener onImageClickedListener) {
        pagerAdapter.setOnImageClickedListener(onImageClickedListener);
    }


    public PositionIndicator getPositionIndicator() {
        return indicator;
    }

    /**
     * @param indicatorLayoutID The root tag of the layout XML file must be some subclass of the
     *                          <code>AutoPlayGallery.PositionIndicator</code>
     * @param layoutParams LayoutParams that used to set the position of the indicator
     */
    public void setPositionIndicator(int indicatorLayoutID, FrameLayout.LayoutParams layoutParams) {
        ViewStub stub = (ViewStub) findViewById(R.id.widget_auto_play_gallery_indicator);
        stub.setLayoutResource(indicatorLayoutID);

        indicator = (PositionIndicator) stub.inflate();
        indicator.setCount(drawables.length);
        indicator.setCurrentPosition(getCurrentItem());

        indicator.setLayoutParams(layoutParams);
    }


    /**
     * Abstract inner class that used to indicate the index of the image that presented by the
     * <code>AutoPlayGallery</code><br />
     *
     * Client application that use the <code>AutoPlayGallery</code> can extend this class and then
     * pass it to the <code>AutoPlayGallery</code>
     *
     * <strong>Note: </strong> the client code that use the AutoPlayGallery can simply ignore this
     * class if he/she don't need the "indicator".
     */
    public abstract static class PositionIndicator extends View {

        public PositionIndicator(Context context) {
            super(context);
        }

        public PositionIndicator(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public PositionIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

//        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//        public PositionIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//            super(context, attrs, defStyleAttr, defStyleRes);
//        }

        /**
         * Set the total number of the indicator
         * @param count total number of the indicator
         */
        abstract void setCount(int count);

        /**
         * Set the current selected position of the indicator
         */
        abstract void setCurrentPosition(int currentPosition);

    }


    /**
     * Callback of the <code>AutoPlayGallery</code><br />
     */
    public interface OnImageClickedListener {
        /**
         * @param index index of the image
         */
        void onClick(int index);
    }

}

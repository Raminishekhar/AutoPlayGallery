package com.jekton.autoplaygallery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * ViewPager that used by AutoPlayGallery to supply several <code>View</code> (actually, ImageView)
 * for the ViewPager
 *
 * @author Jekton Luo
 * @version 0.01 6/7/2015.
 */
public class GalleryPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private Context context;
    private Drawable[] drawables;

    // using an ImageView array to store the views
    private ImageView[] imageViews;
    private AutoPlayGallery.OnImageClickedListener onImageClickedListener;


    /**
     * @param context Context of the application
     * @param drawables Drawables that used in the <code>AutoPlayGallery</code>
     */
    public GalleryPagerAdapter(Context context, Drawable[] drawables) {
        this.context = context;
        this.drawables = drawables;

        init();
    }


    private void init() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        imageViews = new ImageView[drawables.length];
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = new ImageView(context);
            imageViews[i].setLayoutParams(layoutParams);
            imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews[i].setImageDrawable(drawables[i]);
            imageViews[i].setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        if (onImageClickedListener != null) {
            // delivery the the event to OnImageClickListener
            onImageClickedListener.onClick(getItemPosition(v));
        }
    }




    @Override
    public int getCount() {
        return drawables.length;
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews[position]);
        return imageViews[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        for (int i = 0; i < imageViews.length; i++) {
            if (object == imageViews[i])
                return i;
        }
        return 0;
    }

    public AutoPlayGallery.OnImageClickedListener getOnImageClickedListener() {
        return onImageClickedListener;
    }

    public void setOnImageClickedListener(AutoPlayGallery.OnImageClickedListener onImageClickedListener) {
        this.onImageClickedListener = onImageClickedListener;
    }
}

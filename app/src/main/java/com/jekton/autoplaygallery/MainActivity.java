package com.jekton.autoplaygallery;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGallery();
    }

    private void initGallery() {
        Resources resources = getResources();

        Drawable[] drawables = new Drawable[4];
        drawables[0] = resources.getDrawable(R.drawable.activity_home_activities0);
        drawables[1] = resources.getDrawable(R.drawable.activity_home_activities1);
        drawables[2] = resources.getDrawable(R.drawable.activity_home_activities2);
        drawables[3] = resources.getDrawable(R.drawable.activity_home_activities3);

        AutoPlayGallery gallery = (AutoPlayGallery) findViewById(R.id.gallery);
        gallery.setDrawables(drawables);


        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                             ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        layoutParams.bottomMargin = 4;
        gallery.setPositionIndicator(R.layout.widget_auto_play_gallery_indicator, layoutParams);

        gallery.setOnImageClickedListener(new AutoPlayGallery.OnImageClickedListener() {
            @Override
            public void onClick(int index) {
                Toast.makeText(MainActivity.this, "clicked " + index, Toast.LENGTH_SHORT).show();
            }
        });

        gallery.startPlay(2000);
    }

}

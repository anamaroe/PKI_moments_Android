package com.example.moments.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moments.R;
import com.example.moments.databinding.ActivityHomePageBinding;
import com.example.moments.util.BottomMenuNav;

import java.util.Timer;
import java.util.TimerTask;

public class HomePageActivity extends AppCompatActivity {

    private ImageView carouselImage1, carouselImage2, carouselImage3;
    private final int[] carouselPhotos1 = {
            R.drawable.pocetna_lake,
            R.drawable.pocetna_18,
            R.drawable.pocetna_toast
    };

    private final int[] carouselPhotos2 = {
            R.drawable.pocetna_trube,
            R.drawable.pocetna_sto,
            R.drawable.pocetna_sala
    };

    private final int[] carouselPhotos3 = {
            R.drawable.pocetna_putic,
            R.drawable.pocetna_ljudi,
            R.drawable.pocetna_bw
    };

    private int currentIndex1 = 0, currentIndex2 = 0, currentIndex3 = 0;
    private Timer timer = new Timer();
    private final int CAROUSEL_DELAY = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.moments.databinding.ActivityHomePageBinding binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        carouselImage1 = findViewById(R.id.item1);
        carouselImage2 = findViewById(R.id.item2);
        carouselImage3 = findViewById(R.id.item3);

        // bottom menu navigation setup:
        BottomMenuNav.setupNavigation(HomePageActivity.this);

        startAutoSlide();
    }

    private void startAutoSlide() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> switchAllCarousels());
            }
        }, CAROUSEL_DELAY, CAROUSEL_DELAY);
    }

    private void switchAllCarousels() {
        currentIndex1 = applySlideAnimation(carouselImage1, carouselPhotos1, currentIndex1, 500, 0);

        new Handler(Looper.getMainLooper()).postDelayed(() ->
                        currentIndex2 = applySlideAnimation(carouselImage2, carouselPhotos2, currentIndex2, 600, 100),
                100
        );
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                        currentIndex3 = applySlideAnimation(carouselImage3, carouselPhotos3, currentIndex3, 700, 200),
                200
        );
    }

    // SLIDE
    private int applySlideAnimation(ImageView carouselImage, int[] carouselPhotos, int currentIndex, int duration, int delay) {
        TranslateAnimation slideOut = new TranslateAnimation(0, -carouselImage.getWidth(), 0, 0);
        slideOut.setDuration(duration);
        slideOut.setFillAfter(false);

        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                int newIndex = (currentIndex + 1) % carouselPhotos.length;
                carouselImage.setImageResource(carouselPhotos[newIndex]);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    TranslateAnimation slideIn = new TranslateAnimation(carouselImage.getWidth(), 0, 0, 0);
                    slideIn.setDuration(duration);
                    slideIn.setFillAfter(true);
                    carouselImage.startAnimation(slideIn);
                }, delay);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        carouselImage.startAnimation(slideOut);
        return (currentIndex + 1) % carouselPhotos.length;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
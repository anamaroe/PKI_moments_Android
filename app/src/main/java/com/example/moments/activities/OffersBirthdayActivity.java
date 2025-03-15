package com.example.moments.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moments.R;
import com.example.moments.databinding.ActivityOffersBirthdayBinding;
import com.example.moments.util.BottomMenuNav;
import com.example.moments.util.CommentManager;
import com.example.moments.util.EventScheduler;

public class OffersBirthdayActivity extends AppCompatActivity {

    private final String eventType = "r";
    private final int eventPrice = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.moments.databinding.ActivityOffersBirthdayBinding binding = ActivityOffersBirthdayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // bottom menu navigation setup:
        BottomMenuNav.setupNavigation(OffersBirthdayActivity.this);

        // add to cart listener
        Button addToCart = findViewById(R.id.button_add_to_cart_birthday);
        addToCart.setOnClickListener(v -> EventScheduler.openSchedulingDialog(this, eventType, eventPrice));

        // comments
        LinearLayout commentsContainer = findViewById(R.id.comment_container);
        CommentManager.displayComments(this, commentsContainer, eventType);

    }

}
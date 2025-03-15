package com.example.moments.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moments.R;
import com.example.moments.databinding.ActivityOffersAnniverBinding;
import com.example.moments.util.BottomMenuNav;
import com.example.moments.util.CommentManager;
import com.example.moments.util.EventScheduler;

public class OffersAnniverActivity extends AppCompatActivity {

    private final String eventType = "g";
    private final int eventPrice = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.moments.databinding.ActivityOffersAnniverBinding binding = ActivityOffersAnniverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // bottom menu navigation setup:
        BottomMenuNav.setupNavigation(OffersAnniverActivity.this);

        // add to cart listener
        Button addToCart = findViewById(R.id.button_add_to_cart_anniversary);
        addToCart.setOnClickListener(v -> EventScheduler.openSchedulingDialog(this, eventType, eventPrice));

        // comments
        LinearLayout commentsContainer = findViewById(R.id.comment_container);
        CommentManager.displayComments(this, commentsContainer, eventType);
    }
}
package com.example.moments.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moments.R;
import com.example.moments.databinding.ActivityCartBinding;
import com.example.moments.util.BasketManager;
import com.example.moments.util.BottomMenuNav;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.moments.databinding.ActivityCartBinding binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // bottom menu navigation setup:
        BottomMenuNav.setupNavigation(CartActivity.this);

        // show cart contents:
        LinearLayout basketContainer = findViewById(R.id.cart_container);
        BasketManager.displayBasket(this, basketContainer);

    }
}
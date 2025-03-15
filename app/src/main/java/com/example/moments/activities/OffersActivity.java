package com.example.moments.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moments.R;
import com.example.moments.databinding.ActivityOffersBinding;

public class OffersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.moments.databinding.ActivityOffersBinding binding = ActivityOffersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // navigacija ka stranicama dogadjaja:
        LinearLayout birthdayPage = findViewById(R.id.go_birthdays),
                weddingPage = findViewById(R.id.go_weddings),
                eighteenPage = findViewById(R.id.go_18th_bdays),
                anniversaryPage = findViewById(R.id.go_anniversaries),
                gradPage = findViewById(R.id.go_grads),
                corporatePage = findViewById(R.id.go_corporate);

        birthdayPage.setOnClickListener(v -> {
            startActivity(new Intent(OffersActivity.this, OffersBirthdayActivity.class));
        });
        weddingPage.setOnClickListener(v -> {
            startActivity(new Intent(OffersActivity.this, OffersWedActivity.class));
        });
        eighteenPage.setOnClickListener(v -> {
            startActivity(new Intent(OffersActivity.this, Offers18bdayActivity.class));
        });
        anniversaryPage.setOnClickListener(v -> {
            startActivity(new Intent(OffersActivity.this, OffersAnniverActivity.class));
        });
        gradPage.setOnClickListener(v -> {
            startActivity(new Intent(OffersActivity.this, OffersGradActivity.class));
        });
        corporatePage.setOnClickListener(v -> {
            startActivity(new Intent(OffersActivity.this, OffersCorporateActivity.class));
        });

        // navigacija menija:
        LinearLayout homeIcon = findViewById(R.id.go_home),
                aboutIcon = findViewById(R.id.go_about),
                cartIcon = findViewById(R.id.go_cart),
                profileIcon = findViewById(R.id.go_profile);

        homeIcon.setOnClickListener(v -> {
            startActivity(new Intent(OffersActivity.this, HomePageActivity.class));
        });
        aboutIcon.setOnClickListener(v -> {
            startActivity(new Intent(OffersActivity.this, AboutActivity.class));
        });
        cartIcon.setOnClickListener(v -> {
            startActivity(new Intent(OffersActivity.this, CartActivity.class));
        });
        profileIcon.setOnClickListener(v -> {
            startActivity(new Intent(OffersActivity.this, ProfileViewActivity.class));
        });

    }
}
package com.example.moments.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moments.databinding.ActivityAboutBinding;
import com.example.moments.util.BottomMenuNav;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.moments.databinding.ActivityAboutBinding binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // bottom menu navigation setup:
        BottomMenuNav.setupNavigation(AboutActivity.this);

    }
}
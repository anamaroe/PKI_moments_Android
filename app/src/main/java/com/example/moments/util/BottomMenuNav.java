package com.example.moments.util;

import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;

import com.example.moments.activities.AboutActivity;
import com.example.moments.activities.CartActivity;
import com.example.moments.activities.HomePageActivity;
import com.example.moments.activities.OffersActivity;
import com.example.moments.activities.ProfileViewActivity;
import com.example.moments.R;

/*
    Used for setting up a bottom navigation menu on every page of the app.
 */
public class BottomMenuNav {

    public static void setupNavigation(Activity activity) {
        LinearLayout homeIcon = activity.findViewById(R.id.go_home);
        LinearLayout aboutIcon = activity.findViewById(R.id.go_about);
        LinearLayout offerIcon = activity.findViewById(R.id.go_offers);
        LinearLayout cartIcon = activity.findViewById(R.id.go_cart);
        LinearLayout profileIcon = activity.findViewById(R.id.go_profile);

        homeIcon.setOnClickListener(v ->    activity.startActivity(new Intent(activity, HomePageActivity.class)));
        aboutIcon.setOnClickListener(v ->   activity.startActivity(new Intent(activity, AboutActivity.class)));
        offerIcon.setOnClickListener(v ->   activity.startActivity(new Intent(activity, OffersActivity.class)));
        cartIcon.setOnClickListener(v ->    activity.startActivity(new Intent(activity, CartActivity.class)));
        profileIcon.setOnClickListener(v -> activity.startActivity(new Intent(activity, ProfileViewActivity.class)));
    }

}

package com.example.moments.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moments.R;
import com.example.moments.databinding.ActivityProfileViewBinding;
import com.example.moments.util.AppState;
import com.example.moments.util.BottomMenuNav;
import com.example.moments.util.User;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileViewActivity extends AppCompatActivity {

    private static final String PREFS =  "preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.moments.databinding.ActivityProfileViewBinding binding = ActivityProfileViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // bottom menu navigation setup:
        BottomMenuNav.setupNavigation(ProfileViewActivity.this);

        // btnChangeData i btnChangePassword
        Button changeData = findViewById(R.id.btnChangeData),
                changePassword = findViewById(R.id.btnChangePassword);
        changeData.setOnClickListener(v -> {
            startActivity(new Intent(ProfileViewActivity.this, ProfileEditDataActivity.class));
        });
        changePassword.setOnClickListener(v -> {
            startActivity(new Intent(ProfileViewActivity.this, ProfileEditPassActivity.class));
        });

        // notifications
        LinearLayout notifications = findViewById(R.id.profile_notifs);
        notifications.setOnClickListener(v -> openNotificationsDialog());

        // logging out
        LinearLayout loggingOut = findViewById(R.id.profile_logout);
        loggingOut.setOnClickListener(v -> openAreYouSureLogout());

        // show user data
        showUserData();
    }

    protected void openNotificationsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.profile_notifications_dialog);

        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        setDialogSize(dialog);
        dialog.show();
    }

    protected void openAreYouSureLogout() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.cart_item_remove); // tako se zove samo nmg

        TextView message = dialog.findViewById(R.id.dialogMessage);
        message.setText(R.string.da_li_zelite_da_se_odjavite);

        Button btnCancel = dialog.findViewById(R.id.btnNo);
        Button btnConfirm = dialog.findViewById(R.id.btnYes);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            // delete username from prefs and go to main activity
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("username");
            editor.apply();
            startActivity(new Intent(this, MainActivity.class));
        });
        setDialogSize(dialog);
        dialog.show();
    }

    private void showUserData() {
        String usersString = AppState.getInstance(this).getUsers();
        User thisUser = null;
        try {
            JSONArray usersArray = new JSONArray(usersString);
            for (int i = usersArray.length() - 1; i >= 0; i--) {
                JSONObject userObject = usersArray.getJSONObject(i);
                if(userObject.getString("username").equals(AppState.getInstance(this).getUsername())) {
                    thisUser = new User(
                            userObject.getString("username"),
                            userObject.getString("password"),
                            userObject.getString("name"),
                            userObject.getString("surname"),
                            userObject.getString("phone"),
                            userObject.getString("address")
                    );
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(thisUser != null) {
            TextView usernameText = findViewById(R.id.username);
            usernameText.setText(thisUser.getUsername());

            TextView nameText = findViewById(R.id.name);
            nameText.setText(thisUser.getName());

            TextView surnameText = findViewById(R.id.surname);
            surnameText.setText(thisUser.getSurname());

            TextView telephoneText = findViewById(R.id.telephone);
            telephoneText.setText(thisUser.getTelephoneNumber());

            TextView addressText = findViewById(R.id.address);
            addressText.setText(thisUser.getAddress());
        }
    }

    protected void setDialogSize(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (dialog.getContext().getResources().getDisplayMetrics().widthPixels * 0.8);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

}
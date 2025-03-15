package com.example.moments.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moments.R;
import com.example.moments.databinding.ActivityProfileEditDataBinding;
import com.example.moments.util.AppState;
import com.example.moments.util.BottomMenuNav;
import com.example.moments.util.User;


public class ProfileEditDataActivity extends AppCompatActivity {

    private static final String PREFS =  "preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.moments.databinding.ActivityProfileEditDataBinding binding = ActivityProfileEditDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // bottom menu navigation setup:
        BottomMenuNav.setupNavigation(ProfileEditDataActivity.this);

        // setup hints:
        setupUserDataHints(ProfileEditDataActivity.this);

        // notifications
        LinearLayout notifications = findViewById(R.id.profile_notifs);
        notifications.setOnClickListener(v -> openNotificationsDialog());

        // logging out
        LinearLayout loggingOut = findViewById(R.id.profile_logout);
        loggingOut.setOnClickListener(v -> openAreYouSureLogout());

        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(v -> startActivity(new Intent(ProfileEditDataActivity.this, ProfileViewActivity.class)));
        //btnSave.setOnClickListener(v -> saveNewUserData(this));
        btnSave.setOnClickListener(v -> openConfirmDialog(this));
    }

    private void openConfirmDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.event_scheduling_confirmed_dialog);

        TextView msg = dialog.findViewById(R.id.dialogMessage);
        msg.setText("Sačuvano.");

        Button btnClose = dialog.findViewById(R.id.btnOk);
        btnClose.setOnClickListener(v -> {

            // save changes:
            saveNewUserData(activity);

            dialog.dismiss();
            activity.startActivity(new Intent(activity, ProfileViewActivity.class));
            activity.finish();
        });
        setDialogSize(dialog);
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void saveNewUserData(Activity activity) {
        String currentUser = AppState.getInstance(activity).getUsername();

        EditText newName = activity.findViewById(R.id.editName);
        if(!newName.getText().toString().isEmpty()) {
            AppState.getInstance(this).changeUserName(currentUser, newName.getText().toString());
        }

        EditText newSurname = activity.findViewById(R.id.editSurname);
        if(!newSurname.getText().toString().isEmpty()) {
            AppState.getInstance(this).changeUserSurname(currentUser, newSurname.getText().toString());
        }

        EditText newPhone = activity.findViewById(R.id.editTelephone);
        if(!newPhone.getText().toString().isEmpty()) {
            AppState.getInstance(this).changeUserTelephone(currentUser, newPhone.getText().toString());
        }

        EditText newAddress = activity.findViewById(R.id.editAddress);
        if(!newAddress.getText().toString().isEmpty()) {
            AppState.getInstance(this).changeUserAddress(currentUser, newAddress.getText().toString());
        }
    }

    protected static void setDialogSize(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (dialog.getContext().getResources().getDisplayMetrics().widthPixels * 0.8);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    private void setupUserDataHints(Activity activity) {
        // find current user
        String currentUsername = AppState.getInstance(this).getUsername();
        User currentUser = AppState.getInstance(this).getUserByUsername(currentUsername);
        if(currentUser == null) return;
        EditText editUsername = findViewById(R.id.editUsername),
                editName = findViewById(R.id.editName),
                editSurname = findViewById(R.id.editSurname),
                editPhone = findViewById(R.id.editTelephone),
                editAddress = findViewById(R.id.editAddress);
        editUsername.setHint(currentUsername);
        editName.setHint(currentUser.getName());
        editSurname.setHint(currentUser.getSurname());
        editPhone.setHint(currentUser.getTelephoneNumber());
        editAddress.setHint(currentUser.getAddress());
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
}
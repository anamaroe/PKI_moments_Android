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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moments.R;
import com.example.moments.databinding.ActivityProfileEditPassBinding;
import com.example.moments.util.AppState;
import com.example.moments.util.BottomMenuNav;

public class ProfileEditPassActivity extends AppCompatActivity {

    private static final String PREFS =  "preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.moments.databinding.ActivityProfileEditPassBinding binding = ActivityProfileEditPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // bottom menu navigation setup:
        BottomMenuNav.setupNavigation(ProfileEditPassActivity.this);

        // notifications
        LinearLayout notifications = findViewById(R.id.profile_notifs);
        notifications.setOnClickListener(v -> openNotificationsDialog());

        // logging out
        LinearLayout loggingOut = findViewById(R.id.profile_logout);
        loggingOut.setOnClickListener(v -> openAreYouSureLogout());

        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(v -> startActivity(new Intent(ProfileEditPassActivity.this, ProfileViewActivity.class)));
        //btnSave.setOnClickListener(v -> saveNewUserData(this));
        btnSave.setOnClickListener(v -> {
            if(validDataEntered())  openConfirmDialog(this);
        });
    }

    private void openConfirmDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.event_scheduling_confirmed_dialog);

        TextView msg = dialog.findViewById(R.id.dialogMessage);
        msg.setText("Sačuvano.");

        Button btnClose = dialog.findViewById(R.id.btnOk);
        btnClose.setOnClickListener(v -> {

            // save changes:
            saveNewUserPassword(activity);

            dialog.dismiss();
            activity.startActivity(new Intent(activity, ProfileViewActivity.class));
            activity.finish();
        });
        setDialogSize(dialog);
        dialog.show();
    }

    private boolean validDataEntered() {
        EditText oldPass = findViewById(R.id.editOld),
                newPass = findViewById(R.id.editNew),
                newAgain = findViewById(R.id.editNewAgain);
        if(oldPass.getText().toString().isEmpty() ||
                newPass.getText().toString().isEmpty() ||
                newAgain.getText().toString().isEmpty()) {
            Toast.makeText(this, "Nisu uneseni svi podaci.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!newPass.getText().toString().equals(newAgain.getText().toString())) {
            Toast.makeText(this.getApplicationContext(), "Lozinke se ne podudaraju.", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!oldPass.getText().toString().equals(AppState.getInstance(this).getPassword())) {
            Toast.makeText(this.getApplicationContext(), "Neispravna lozinka", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void saveNewUserPassword(Activity activity) {
        String currentUser = AppState.getInstance(activity).getUsername();
        EditText oldPass = activity.findViewById(R.id.editOld),
                newPass = activity.findViewById(R.id.editNew),
                newAgain = activity.findViewById(R.id.editNewAgain);

        // znamo da je sve uneseno
        AppState.getInstance(this).changeUserPassword(currentUser, oldPass.getText().toString(), newPass.getText().toString());
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
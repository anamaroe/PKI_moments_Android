package com.example.moments.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.moments.R;

import java.util.Calendar;
import java.util.Objects;

public class EventScheduler {

    @SuppressLint("SetTextI18n")
    public static void openSchedulingDialog(Context context, String eventType, int eventPrice) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.event_scheduling_dialog);

        DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        EditText etGuests = dialog.findViewById(R.id.etGuests);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            String guests = etGuests.getText().toString();

            if (guests.isEmpty()) {
                Toast.makeText(context, "Unesite broj gostiju", Toast.LENGTH_SHORT).show();
            } else {
                @SuppressLint("DefaultLocale") String formattedDate = String.format("%02d-%02d-%04d", day, month, year);
                showConfirmDialog(context, dialog, guests, formattedDate, eventType, eventPrice);
            }
        });

        // set today's date and block past dates
        Calendar today = Calendar.getInstance();
        datePicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        long todayMillis = System.currentTimeMillis();
        datePicker.setMinDate(todayMillis);

        TextView dialogPrice = dialog.findViewById(R.id.dialogPrice);
        dialogPrice.setText("Cena događaja je " + eventPrice + " €.");
        setDialogSize(dialog);
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private static void showConfirmDialog(Context context, Dialog parentDialog, String guests, String formattedDate, String eventType, int eventPrice) {
        Dialog confirmDialog = new Dialog(context);
        confirmDialog.setContentView(R.layout.event_scheduling_confirmed_dialog);
        TextView confirmText = confirmDialog.findViewById(R.id.dialogMessage);

        String eventString, addedToCart = "dodat";
        if(Objects.equals(eventType, "r")) {
            eventString = "Rođendan";
        } else if(Objects.equals(eventType, "v")) {
            eventString = "Venčanje";
            addedToCart += "o";
        } else if(Objects.equals(eventType, "p")) {
            eventString = "Punoletstvo";
            addedToCart += "o";
        } else if(Objects.equals(eventType, "g")) {
            eventString = "Godišnjica";
            addedToCart += "a";
        } else if(Objects.equals(eventType, "m")) {
            eventString = "Matura";
            addedToCart += "a";
        } else {
            eventString = "Korporativni događaj";
        }
        confirmText.setText(eventString + " za " + formattedDate + " sa " + guests + " gostiju je " + addedToCart + " u korpu");
        Button btnOk = confirmDialog.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(v -> {
            AppState.getInstance(context).saveEvent(guests, formattedDate, eventType, eventPrice);
            Toast.makeText(context, "Zakazivanje potvrđeno!", Toast.LENGTH_LONG).show();
            confirmDialog.dismiss();
            parentDialog.dismiss();
        });

        setDialogSize(confirmDialog);
        confirmDialog.show();
    }

    private static void setDialogSize(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (dialog.getContext().getResources().getDisplayMetrics().widthPixels * 0.8);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

}


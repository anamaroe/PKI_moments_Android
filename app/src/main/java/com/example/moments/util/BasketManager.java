package com.example.moments.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moments.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasketManager {

    public static void displayBasket(Activity activity, LinearLayout basketContainer) {
        basketContainer.removeAllViews();

        String loggedInUser = AppState.getInstance(activity).getUsername();
        String basketString = AppState.getInstance(activity).getEvents();
        List<Event> scheduledEvents = new ArrayList<>();

        try {
            JSONArray basketArray = new JSONArray(basketString);
            for (int i = 0; i < basketArray.length(); i++) {
                JSONObject basketObject = basketArray.getJSONObject(i);

                UUID id = UUID.fromString(basketObject.getString("id"));
                int numOfGuests = Integer.parseInt(basketObject.getString("num"));
                String date = basketObject.getString("date");
                String eventType = basketObject.getString("type");
                int price = Integer.parseInt(basketObject.getString("price"));
                boolean confirmed = basketObject.getBoolean("confirmed");
                String username = basketObject.getString("username");

                // showing only contents of current user's cart
                if(username.equals(loggedInUser)) {
                    scheduledEvents.add(new Event(id, numOfGuests, date, eventType, price, confirmed, username));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if empty display text:
        if(scheduledEvents.isEmpty()) {
            TextView emptyBasketText = activity.findViewById(R.id.ifCartEmpty);
            emptyBasketText.setText(R.string.korpa_je_prazna);
            return;
        }

        // add to a view
        for (Event event : scheduledEvents) {
            View cartItemView;
            if(event.isConfirmed()) {
                cartItemView = activity.getLayoutInflater().inflate(R.layout.cart_item_confirmed, basketContainer, false);
            } else {
                cartItemView = activity.getLayoutInflater().inflate(R.layout.cart_item_pending, basketContainer, false);
                /// buttons...
                cartItemView.findViewById(R.id.btnRemove).setOnClickListener(v ->
                        openRemoveItemFromCartDialog(activity, event, basketContainer)
                );
                cartItemView.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
                    // this event becomes confirmed:
                    confirmBasketEvent(activity, event, basketContainer);
                });
            }
            // title and photo
            String eventTitle, currentEventType = event.getEventType();
            ImageView imageView = cartItemView.findViewById(R.id.eventImage);
            TextView cartEventTitle = cartItemView.findViewById(R.id.eventTitle);

            switch (currentEventType) {
                case "r":
                    eventTitle = "ROĐENDAN";
                    imageView.setImageResource(R.drawable.ponude_rodjendan);
                    break;
                case "v":
                    eventTitle = "VENČANJE";
                    imageView.setImageResource(R.drawable.ponude_wedi);
                    break;
                case "p":
                    eventTitle = "PUNOLETSTVO";
                    imageView.setImageResource(R.drawable.ponude_18);
                    break;
                case "m":
                    eventTitle = "MATURA";
                    imageView.setImageResource(R.drawable.ponude_matura);
                    break;
                case "k":
                    eventTitle = "KORPORATIVNI DOGAĐAJ";
                    imageView.setImageResource(R.drawable.ponude_korp);
                    break;
                default:
                    eventTitle = "GODIŠNJICA";
                    imageView.setImageResource(R.drawable.ponude_god);
                    break;
            }
            cartEventTitle.setText(eventTitle);

            // date
            TextView cartEventDate = cartItemView.findViewById(R.id.eventDate);
            cartEventDate.setText(event.getDate());

            // guest
            TextView cartEventGuestsCount = cartItemView.findViewById(R.id.eventGuests);
            cartEventGuestsCount.setText(String.format("%s%s", activity.getString(R.string.broj_gostiju_korpa),
                    String.valueOf(event.getNumOfGuests())));

            // price
            TextView cartEventPrice = cartItemView.findViewById(R.id.eventPrice);
            cartEventPrice.setText(String.format("%s%s", String.valueOf(event.getPrice()),
                    activity.getString(R.string.euro)));

            basketContainer.addView(cartItemView);
        }
    }

    public static void openRemoveItemFromCartDialog(Activity activity, Event event, LinearLayout container) {
        // zovi metodu iz appstate i opet display...

        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.cart_item_remove);

        TextView msg = dialog.findViewById(R.id.dialogMessage);

        String eventTitle, currentEventType = event.getEventType();
        switch (currentEventType) {
            case "r":
                eventTitle = "Rođendan";
                break;
            case "v":
                eventTitle = "Venčanje";
                break;
            case "p":
                eventTitle = "Punoletstvo";
                break;
            case "m":
                eventTitle = "Matura";
                break;
            case "k":
                eventTitle = "Korporativni događaj";
                break;
            default:
                eventTitle = "Godišnjica";
                break;
        }
        msg.setText(String.format("Ukloni iz korpe:\n%s za %s, broj gostiju: %d", eventTitle, event.getDate(), event.getNumOfGuests()));

        Button btnCancel = dialog.findViewById(R.id.btnNo);
        Button btnConfirm = dialog.findViewById(R.id.btnYes);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            showConfirmItemRemovalDialog(activity, dialog, event, container);
        });
        setDialogSize(dialog);
        dialog.show();
    }

    private static void showConfirmItemRemovalDialog(Activity activity,
                                                      Dialog parentDialog,
                                                      Event eventToRemove,
                                                      LinearLayout container) {

        Dialog confirmDialog = new Dialog(activity);
        confirmDialog.setContentView(R.layout.event_scheduling_confirmed_dialog);

        TextView confirmText = confirmDialog.findViewById(R.id.dialogMessage);
        confirmText.setText("Uklonjeno.");

        Button btnOk = confirmDialog.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(v -> {
            // remove this event from scheduled events
            AppState.getInstance(activity.getApplicationContext()).removeEvent(eventToRemove.getId());

            Toast.makeText(activity,
                    "Dogadjaj je uklonjen iz korpe.",
                    Toast.LENGTH_LONG).show();

            displayBasket(activity, container);

            confirmDialog.dismiss();
            parentDialog.dismiss();
        });
        setDialogSize(confirmDialog);
        confirmDialog.show();
    }

    public static void confirmBasketEvent(Activity activity, Event event, LinearLayout container) {
        // change status and display the cart contents again
        AppState.getInstance(activity.getApplicationContext()).confirmEvent(event.getId());

        Toast.makeText(activity,
                "Dogadjaj je potvrdjen.",
                Toast.LENGTH_LONG).show();

        displayBasket(activity, container);
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

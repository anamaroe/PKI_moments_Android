package com.example.moments.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.moments.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/*
    For displaying and managing comments and all necessary dialogs.
    Contains default comments.
    Adds user's new comments to the view.
 */
public class CommentManager {

    private static final List<Comment> defaultComments = new ArrayList<>(Arrays.asList(
            new Comment(4, "Fantasticno! Proslava je bila sjajna, sve je bilo lepo organizovano, ali mislim da bi dekoracija mogla biti malo bolja. Sve u svemu, zadovoljni smo!", "d"),
            new Comment(5, "Super!", "d"),
            new Comment(3, "Super sramite se cestitke", "d")
    ));

    // load comments
    public static void displayComments(Activity activity, LinearLayout commentsContainer, String eventType) {
        commentsContainer.removeAllViews();

        List<Comment> comments = new ArrayList<>();
        String commentsString = AppState.getInstance(activity).getComments();

        try {
            JSONArray commentsArray = new JSONArray(commentsString);
            for (int i = commentsArray.length() - 1; i >= 0; i--) {
                JSONObject commentObject = commentsArray.getJSONObject(i);

                int rating = commentObject.getInt("rating");
                String commentText = commentObject.getString("comment");
                String event = commentObject.getString("event");
                if (event.equals(eventType)) {
                    comments.add(new Comment(rating, commentText, event));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(Objects.equals(eventType, "r") || Objects.equals(eventType, "k")) {
            comments.addAll(defaultComments);
        } else if(Objects.equals(eventType, "p") || Objects.equals(eventType, "v")) {
            comments.add(defaultComments.get(0));
            comments.add(defaultComments.get(1));
        } else {
            comments.add(defaultComments.get(1));
        }

        // add to a view
        for (Comment comment : comments) {
            View commentView = activity.getLayoutInflater().inflate(R.layout.comment, commentsContainer, false);
            TextView commentText = commentView.findViewById(R.id.commentText);
            commentText.setText(comment.getComment());
            LinearLayout ratingStars = commentView.findViewById(R.id.ratingStars);
            for (int i = 1; i <= 5; i++) {
                ImageView star = new ImageView(activity);
                star.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                star.setImageResource(R.drawable.star);
                if (i <= comment.getRating()) {
                    star.setImageAlpha(170);
                } else {
                    star.setImageAlpha(60);
                }
                ratingStars.addView(star);
            }
            commentsContainer.addView(commentView);
        }
        // add comment btn
        View addCommentView = activity.getLayoutInflater().inflate(R.layout.comment_adder, commentsContainer, false);
        addCommentView.findViewById(R.id.add_comment).setOnClickListener(v -> {
            openCommentDialog(activity, eventType, commentsContainer);
        });
        commentsContainer.addView(addCommentView);
    }

    // new comment dialog
    public static void openCommentDialog(Context context, String event, LinearLayout commentsContainer) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.comment_editor);

        EditText rating = dialog.findViewById(R.id.rating);
        EditText comment = dialog.findViewById(R.id.comment);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            try {
                int rate = Integer.parseInt(rating.getText().toString().trim());
                String newComment = comment.getText().toString();

                if (newComment.isEmpty() || rate < 1 || rate > 5) {
                    Toast.makeText(context, "Molimo unesite validne podatke.", Toast.LENGTH_SHORT).show();
                } else {
                    showConfirmCommentDialog(context, dialog, rate, newComment, event, commentsContainer);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Molimo unesite validan broj za ocenu.", Toast.LENGTH_SHORT).show();
            }
        });
        setDialogSize(dialog);
        dialog.show();
    }

    // new comment confirmed dialog
    private static void showConfirmCommentDialog(Context context, Dialog parentDialog, int rating, String comment, String event, LinearLayout commentsContainer) {
        Dialog confirmDialog = new Dialog(context);
        confirmDialog.setContentView(R.layout.event_scheduling_confirmed_dialog);

        TextView confirmText = confirmDialog.findViewById(R.id.dialogMessage);
        confirmText.setText("Vaš komentar je dodat.");

        Button btnOk = confirmDialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> {
            // Saving the comment
            AppState appState = AppState.getInstance(context);
            appState.saveComment(rating, comment, event);

            Toast.makeText(context, "Vaš komentar je dodat.", Toast.LENGTH_LONG).show();

            if (context instanceof Activity) {
                // Assuming the container is passed when calling this method
                // LinearLayout commentsContainer = ((Activity) context).findViewById(R.id.commentsContainer);
                displayComments((Activity) context, commentsContainer, event);
            }

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

package com.example.moments.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

/*
    This class stores the app's data, including user login information,
    newly entered comments, and the user's cart content.

    There is only one instance of this class.

    Upon startup, the app loads the default user data.
    Upon shutdown, all entered data (modified user data, cart content, and new comments) is deleted.
 */
public class AppState {

    private static final String PREFS = "preferences";
    private static AppState instance;
    private SharedPreferences sharedPreferences;

    // constructor
    private AppState(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static synchronized AppState getInstance(Context context) {
        if (instance == null) {
            instance = new AppState(context.getApplicationContext());
        }
        return instance;
    }

    // login data saving
    // all users are loaded to preferences here
    public void saveLoginData(String username, String password) {
        sharedPreferences.edit()
            .putString("username", username)
            .putString("password", password)
            .apply();
        loadUsers();
    }

    public String getUsername() {
        return sharedPreferences.getString("username", null);
    }

    public String getPassword() {
        return sharedPreferences.getString("password", null);
    }

    // save comment
    public void saveComment(int rating, String comment, String event) {
        try {
            // loading existing data
            String commentsString = sharedPreferences.getString("comments", "[]");
            JSONArray commentsArray = new JSONArray(commentsString);

            // add a new comment
            JSONObject newComment = new JSONObject();
            newComment.put("rating", rating);
            newComment.put("comment", comment);
            newComment.put("event", event);

            commentsArray.put(newComment);

            // save edited array
            sharedPreferences.edit()
                    .putString("comments", commentsArray.toString())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get my comments
    public String getComments() {
        return sharedPreferences.getString("comments", "[]");
    }

    // event saving
    public void saveEvent(String num, String date, String type, int price) {
        try {
            // loading existing data
            String eventsString = sharedPreferences.getString("events", "[]");
            JSONArray eventsArray = new JSONArray(eventsString);

            // add a new event
            JSONObject newEvent = new JSONObject();
            newEvent.put("id", UUID.randomUUID().toString());
            newEvent.put("num", num);
            newEvent.put("date", date);
            newEvent.put("type", type);
            newEvent.put("price", price);
            newEvent.put("confirmed", false);
            newEvent.put("username", getUsername());

            eventsArray.put(newEvent);

            // save edited array
            sharedPreferences.edit()
                    .putString("events", eventsArray.toString())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get events
    public String getEvents() {
        return sharedPreferences.getString("events", "[]");
    }

    // remove event from the cart
    public void removeEvent(UUID eventId) {
        try {
            String eventsString = sharedPreferences.getString("events", "[]");
            JSONArray eventsArray = new JSONArray(eventsString);
            JSONArray updatedArray = new JSONArray();

            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject event = eventsArray.getJSONObject(i);
                String eventUUID = event.getString("id");

                if (!eventUUID.equals(eventId.toString())) {
                    updatedArray.put(event);
                }
            }

            sharedPreferences.edit()
                    .putString("events", updatedArray.toString())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirmEvent(UUID eventId) {
        try {
            String eventsString = sharedPreferences.getString("events", "[]");
            JSONArray eventsArray = new JSONArray(eventsString);

            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject event = eventsArray.getJSONObject(i);
                if (event.getString("id").equals(eventId.toString())) {
                    event.put("confirmed", true);
                    sharedPreferences.edit()
                            .putString("events", eventsArray.toString())
                            .apply();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUsers() {
        try {
            String usersString = sharedPreferences.getString("users", "[]");
            JSONArray usersArray = new JSONArray(usersString);

            JSONObject userOne = new JSONObject();
            userOne.put("username", "admin");
            userOne.put("password", "a");
            userOne.put("name", "Admin");
            userOne.put("surname", "Adminic");
            userOne.put("phone", "067 408 431");
            userOne.put("address", "Bulevar kralja Aleksandra 77");
            usersArray.put(userOne);

            JSONObject userTwo = new JSONObject();
            userTwo.put("username", "pera");
            userTwo.put("password", "p");
            userTwo.put("name", "Pera");
            userTwo.put("surname", "Peric");
            userTwo.put("phone", "067 557 465");
            userTwo.put("address", "Spasica i Masere 49 a");
            usersArray.put(userTwo);

            sharedPreferences.edit()
                    .putString("users", usersArray.toString())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUsers() {
        return sharedPreferences.getString("users", "[]");
    }

    public void changeUserPassword(String username, String oldPassword, String newPassword) {
        try {
            String usersString = sharedPreferences.getString("users", "[]");
            JSONArray usersArray = new JSONArray(usersString);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                if (user.getString("username").equals(username) &&
                        user.getString("password").equals(oldPassword)
                ) {
                    user.put("password", newPassword);
                    break;
                }
            }
            // promeni
            sharedPreferences.edit()
                    .putString("password", newPassword)
                    .apply();
            sharedPreferences.edit()
                    .putString("users", usersArray.toString())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeUserName(String username, String newName) {
        try {
            String usersString = sharedPreferences.getString("users", "[]");
            JSONArray usersArray = new JSONArray(usersString);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                if (user.getString("username").equals(username)) {
                    user.put("name", newName);
                    break;
                }
            }
            sharedPreferences.edit()
                    .putString("users", usersArray.toString())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeUserSurname(String username, String surname) {
        try {
            String usersString = sharedPreferences.getString("users", "[]");
            JSONArray usersArray = new JSONArray(usersString);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                if (user.getString("username").equals(username)) {
                    user.put("surname", surname);
                    break;
                }
            }
            sharedPreferences.edit()
                    .putString("users", usersArray.toString())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeUserTelephone(String username, String tel) {
        try {
            String usersString = sharedPreferences.getString("users", "[]");
            JSONArray usersArray = new JSONArray(usersString);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                if (user.getString("username").equals(username)) {
                    user.put("phone", tel);
                    break;
                }
            }
            sharedPreferences.edit()
                    .putString("users", usersArray.toString())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeUserAddress(String username, String address) {
        try {
            String usersString = sharedPreferences.getString("users", "[]");
            JSONArray usersArray = new JSONArray(usersString);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                if (user.getString("username").equals(username)) {
                    user.put("address", address);
                    break;
                }
            }
            sharedPreferences.edit()
                    .putString("users", usersArray.toString())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUserByUsername(String username) {
        try {
            String usersString = sharedPreferences.getString("users", "[]");
            JSONArray usersArray = new JSONArray(usersString);
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                if (userObject.getString("username").equals(username)) {
                    return new User(
                            userObject.getString("username"),
                            userObject.getString("password"),
                            userObject.getString("name"),
                            userObject.getString("surname"),
                            userObject.getString("phone"),
                            userObject.getString("address")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

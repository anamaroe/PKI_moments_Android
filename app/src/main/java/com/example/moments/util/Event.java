package com.example.moments.util;

import java.util.UUID;

/*
    For basket preview.
 */
public class Event {

    private UUID id;

    private int numOfGuests;

    // format: %02d-%02d-%04d (dd-mm-yyyy)
    private String date;

    // r, v, p, g, m, k
    private String eventType;

    private int price;

    // possible values: false = pending, true = confirmed
    private boolean confirmed;

    private String username;

    public Event(UUID id, int numOfGuests, String date, String eventType, int price, boolean confirmed, String username) {
        this.id = id;
        this.numOfGuests = numOfGuests;
        this.date = date;
        this.eventType = eventType;
        this.price = price;
        this.confirmed = confirmed;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getNumOfGuests() {
        return numOfGuests;
    }

    public void setNumOfGuests(int numOfGuests) {
        this.numOfGuests = numOfGuests;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

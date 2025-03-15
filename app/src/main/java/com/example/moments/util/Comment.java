package com.example.moments.util;

/*
    A class for embodying saved user's comment objects.
 */
public class Comment {
    private int rating;
    private String comment;

    /*
        r - rodjendan
        v - vencanje
        p - punoletstvo
        g - godisnjica
        m - matura
        k - korporativni dogadjaj
     */
    private String event;

    public Comment(int rating, String comment, String event) {
        this.rating = rating;
        this.comment = comment;
        this.event = event;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}

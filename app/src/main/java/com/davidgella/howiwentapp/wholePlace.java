package com.davidgella.howiwentapp;

public class wholePlace {
    public String from, to, user_id;

    public wholePlace(){

    }

    public wholePlace( String from, String to, String user_id){
        this.from = from;
        this.to = to;
        this.user_id = user_id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getUser_id() {
        return user_id;
    }
}

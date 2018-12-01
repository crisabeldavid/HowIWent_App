package com.davidgella.howiwentapp;

public class wholePlace {
    public String from, to, user_id, key;


    public wholePlace(){

    }

    public wholePlace( String from, String to, String user_id, String key){
        this.from = from;
        this.to = to;
        this.user_id = user_id;
        this.key = key;
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

    public String getKey() {
        return key;
    }



}

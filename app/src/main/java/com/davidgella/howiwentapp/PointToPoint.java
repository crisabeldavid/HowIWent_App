package com.davidgella.howiwentapp;

public class PointToPoint {

    public String from, to, mot, fare, comment, wp_id, key;

    public PointToPoint (){

    }

    public PointToPoint ( String from, String to, String mot, String fare, String comment, String wp_id, String key){
        this.from = from;
        this.to = to;
        this.mot = mot;
        this.fare = fare;
        this.comment = comment;
        this.wp_id = wp_id;
        this.key = key;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMot() {
        return mot;
    }

    public void setMot(String mot) {
        this.mot = mot;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getWp_id() {
        return wp_id;
    }

    public void setWp_id(String wp_id) {
        this.wp_id = wp_id;
    }

    public String getKey() {
        return key;
    }


}

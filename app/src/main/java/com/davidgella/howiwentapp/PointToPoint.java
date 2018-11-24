package com.davidgella.howiwentapp;

public class PointToPoint {

    public String from, to, mot, fare, comment, wp_id;

    public PointToPoint (){

    }

    public PointToPoint ( String from, String to, String mot, String fare, String comment, String wp_id){
        this.from = from;
        this.to = to;
        this.mot = mot;
        this.fare = fare;
        this.comment = comment;
        this.wp_id = wp_id;
    }
}

package com.davidgella.howiwentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ViewCard1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcard1page);
    }

    public void toLogIn (View v) {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }

    public void toProfile (View v) {
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }

    public void toViewCard2 (View v) {
        Intent i = new Intent(this,ViewCard2Activity.class);
        startActivity(i);
    }
}

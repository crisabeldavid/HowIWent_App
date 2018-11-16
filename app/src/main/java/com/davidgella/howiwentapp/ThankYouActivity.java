package com.davidgella.howiwentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ThankYouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyoupage);
    }

    public void Back (View v) {
        finish();
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }
}
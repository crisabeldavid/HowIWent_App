package com.davidgella.howiwentapp;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCard2Activity extends AppCompatActivity  implements View.OnClickListener {
    FirebaseAuth mAuth;
    TextView tvUsername, tvFrom, tvTo, tvComment, tvFare, tvMOT;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcard2page);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        mAuth = FirebaseAuth.getInstance();
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvTo = (TextView) findViewById(R.id.tvTo);
        tvFare = (TextView) findViewById(R.id.tvFare);
        tvComment = (TextView) findViewById(R.id.tvComment);
        tvMOT = (TextView) findViewById(R.id.tvMOT);


        loadUserInformation();

        findViewById(R.id.ivSignOut).setOnClickListener(this);
    }








    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();

        // Get a reference to our users
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Users").orderByChild("id").equalTo(user.getUid());

        Intent i = getIntent();
        String id = i.getStringExtra(ViewCard1Activity.P2P_ID);
        Query query2 = FirebaseDatabase.getInstance().getReference("PointToPoint").orderByChild("key").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot Users : dataSnapshot.getChildren()) {
                        tvUsername.setText(Users.child("username").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> transpo_array = new ArrayList<String>();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot PointToPoint : dataSnapshot.getChildren()) {
                        tvFrom.setText(PointToPoint.child("from").getValue().toString());
                        tvTo.setText(PointToPoint.child("to").getValue().toString());
                        tvFare.setText(PointToPoint.child("fare").getValue().toString());
                        tvComment.setText(PointToPoint.child("comment").getValue().toString());
                        tvMOT.setText(PointToPoint.child("mot").getValue().toString());





                    }

                }
            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }






    public void toProfile (View v) {
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ivSignOut:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
}

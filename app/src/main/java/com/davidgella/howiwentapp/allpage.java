package com.davidgella.howiwentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class allpage extends AppCompatActivity implements View.OnClickListener {
    public static final String WHOLEPLACE_ID = "user_id";
    FirebaseAuth mAuth;
    TextView tvUsername, tvPlace;
    ProgressBar progressBar;

    DatabaseReference databaseUsers;
    DatabaseReference databaseWholeList;

    ListView listViewWholePlace;
    List<wholePlace> wholePlaceList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seelatestpage);
        mAuth = FirebaseAuth.getInstance();
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvPlace = (TextView) findViewById(R.id.tvPlace);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        databaseWholeList = FirebaseDatabase.getInstance().getReference("wholePlace");

        listViewWholePlace = (ListView) findViewById(R.id.listViewWholePlace);
        wholePlaceList2 = new ArrayList<>();

        listViewWholePlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wholePlace wholeplace = wholePlaceList2.get(position);
                Intent i = new Intent(getApplicationContext(),ViewCard1Activity.class);

                i.putExtra(WHOLEPLACE_ID, wholeplace.getKey());
                startActivity(i);
            }
        });

        loadUserInformation();
        findViewById(R.id.ivSignOut).setOnClickListener(this);
    }



    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        tvPlace.setText("All");

        // Get a reference to our users
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Users").orderByChild("id").equalTo(user.getUid());
        Query query2 =  FirebaseDatabase.getInstance().getReference("wholePlace");
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


        //list view
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                wholePlaceList2.clear();
                for(DataSnapshot wholePlaceSnapshot: dataSnapshot.getChildren()){
                    wholePlace wholePlace = wholePlaceSnapshot.getValue(wholePlace.class);
                    wholePlaceList2.add(wholePlace);
                }

                ArrayAdapter adapter = new wholePlaceList2(allpage.this, wholePlaceList2);
                listViewWholePlace.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSignOut:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();




        //choose


        if (mAuth.getCurrentUser() == null) {
            //handle already login
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    public void toProfile (View v) {
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }

}

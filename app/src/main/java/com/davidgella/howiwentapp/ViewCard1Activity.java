package com.davidgella.howiwentapp;

import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewCard1Activity extends AppCompatActivity implements View.OnClickListener {
    TextView tvUsername, tvFrom2, tvTo2;
    FirebaseAuth mAuth;
    ListView listViewWholePlace;
    List<PointToPoint>  PointToPointList;
    public static final String P2P_ID = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcard1page);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvTo2 = (TextView) findViewById(R.id.tvTo2);
        tvFrom2 = (TextView) findViewById(R.id.tvFrom2);
        mAuth = FirebaseAuth.getInstance();

        listViewWholePlace = (ListView) findViewById(R.id.listViewWholePlace);
        PointToPointList = new ArrayList<>();

        listViewWholePlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PointToPoint p2p = PointToPointList.get(position);
                Intent i = new Intent(getApplicationContext(),ViewCard2Activity.class);
                i.putExtra(P2P_ID, p2p.getKey());
                startActivity(i);
            }
        });


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
        String id = i.getStringExtra(ProfileActivity.WHOLEPLACE_ID);
        Query query2 =  FirebaseDatabase.getInstance().getReference("PointToPoint").orderByChild("wp_id").equalTo(id);
        Query query3 =  FirebaseDatabase.getInstance().getReference("wholePlace").orderByChild("key").equalTo(id);

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
        //list
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                PointToPointList.clear();
                for(DataSnapshot wholePlaceSnapshot: dataSnapshot.getChildren()){
                    PointToPoint p2p = wholePlaceSnapshot.getValue(PointToPoint.class);
                    PointToPointList.add(p2p);
                }

               ArrayAdapter adapter = new PointToPointList(ViewCard1Activity.this, PointToPointList);
               listViewWholePlace.setAdapter(adapter);
            }
//
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        query3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot wholePlaceSnapshot: dataSnapshot.getChildren()){
                    tvFrom2.setText(wholePlaceSnapshot.child("from").getValue().toString());
                    tvTo2.setText(wholePlaceSnapshot.child("to").getValue().toString());
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

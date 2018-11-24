package com.davidgella.howiwentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Contribute1Activity extends AppCompatActivity implements View.OnClickListener {
    EditText etFrom, etTo;
    TextView tvUsername;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    DatabaseReference databaseUsers, root;
    FirebaseDatabase db;
    private static String z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contributepage1);



        etFrom = (EditText) findViewById(R.id.etFrom);
        etTo = (EditText) findViewById(R.id.etTo);
        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        db = FirebaseDatabase.getInstance();
        root = db.getReference("wholePlace");
        loadUserInformation();
        findViewById(R.id.btnNext).setOnClickListener(this);
        findViewById(R.id.ivSignOut).setOnClickListener(this);


    }
    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();

        // Get a reference to our users
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Users").orderByChild("id").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot Users : dataSnapshot.getChildren()) {
                        tvUsername.setText(Users.child("username").getValue().toString());
//                        System.out.println("HEY: " + Users.child("username").getValue());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnNext:
                   addDifferPlace();
//                    Intent j = new Intent(this,Contribute2Activity.class);
//                    startActivity(j);
                    Intent intent = new Intent(this, Contribute2Activity.class);
                    intent.putExtra("wp_id", z.toString());
                    startActivity(intent);

                    break;
                case R.id.ivSignOut:
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    break;
            }
        }

    private void addDifferPlace() {
        progressBar.setVisibility(View.VISIBLE);
        String from = etFrom.getText().toString().trim();
        String to = etTo.getText().toString().trim();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String key = root.push().getKey();
        z = key;
        wholePlace place = new wholePlace(from,to,id);
        root.child(key).setValue(place);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this,"record added to database",Toast.LENGTH_LONG).show();
    }


    public void toProfile (View v) {
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }

}

package com.davidgella.howiwentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class Contribute2Activity extends AppCompatActivity implements  View.OnClickListener{
    //form values
    EditText etFrom, etTo, etComment, etFare;
    TextView tvUsername;
    ProgressBar progressBar;
    Spinner sMOT;
    FirebaseAuth mAuth;
    DatabaseReference databaseUsers, root;
    FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contributepage2);


        //Spinner
        Spinner spinner = (Spinner) findViewById(R.id.sMOT);
        // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.transpo_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
                spinner.setAdapter(adapter);
        etFrom = (EditText) findViewById(R.id.etFrom);
        etTo = (EditText) findViewById(R.id.etTo);
        etComment = (EditText) findViewById(R.id.etComment);
        etFare = (EditText) findViewById(R.id.etFare);
        sMOT = (Spinner) findViewById(R.id.sMOT);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        db = FirebaseDatabase.getInstance();
        root = db.getReference("PointToPoint");
        loadUserInformation();


        findViewById(R.id.btnAdd).setOnClickListener(this);
        findViewById(R.id.btnDone).setOnClickListener(this);
        findViewById(R.id.ivBack).setOnClickListener(this);



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
            case R.id.btnAdd:
                  addPtoP();

                break;
            case R.id.ivSignOut:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.btnDone:
                Intent j = new Intent(this,ProfileActivity.class);
                j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                finish();
                startActivity(j);
            case R.id.ivBack:
                Intent h = new Intent(this,ProfileActivity.class);
                startActivity(h);
        }
    }


    private void addPtoP() {
        progressBar.setVisibility(View.VISIBLE);
        String from = etFrom.getText().toString().trim();
        String to = etTo.getText().toString().trim();
        String comment = etComment.getText().toString();
        String fare = etFare.getText().toString();
        String mot = sMOT.getSelectedItem().toString();
        String key = root.push().getKey();
        String wp_id = "";
        try {
            Intent intent = getIntent();

             wp_id = intent.getStringExtra("wp_id");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getStringExtra_EX", e + "");
        }



        PointToPoint subplace = new PointToPoint(from,to,mot,fare,comment, wp_id);
        root.child(key).setValue(subplace);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this,"record added to database",Toast.LENGTH_LONG).show();

    }



}

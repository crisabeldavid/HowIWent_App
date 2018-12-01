package com.davidgella.howiwentapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.usage.NetworkStats;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String WHOLEPLACE_ID = "user_id";
    public static final String wp_id = "wp_id";
    FirebaseAuth mAuth;
    TextView tvUsername;
    ProgressBar progressBar;
    ImageButton ivDelete;

    DatabaseReference databaseWholeList;

    ListView listViewWholePlace;
    List<wholePlace> wholePlaceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepage);
        mAuth = FirebaseAuth.getInstance();
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        listViewWholePlace = (ListView) findViewById(R.id.listViewWholePlace);
        wholePlaceList = new ArrayList<>();

        listViewWholePlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wholePlace wholeplace = wholePlaceList.get(position);
                Intent i = new Intent(getApplicationContext(),update_ptop.class);

                i.putExtra(WHOLEPLACE_ID, wholeplace.getKey());
                startActivity(i);
            }
        });

        listViewWholePlace.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                wholePlace wPlace = wholePlaceList.get(position);
                showUpdateDialog(wPlace.getKey(),wPlace.getTo(),wPlace.getFrom());

                return false;
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
        Query query2 =  FirebaseDatabase.getInstance().getReference("wholePlace").orderByChild("user_id").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot Users : dataSnapshot.getChildren()) {
                        tvUsername.setText(Users.child("username").getValue().toString());;
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
//
                wholePlaceList.clear();
                for(DataSnapshot wholePlaceSnapshot: dataSnapshot.getChildren()){
                    wholePlace wholePlace = wholePlaceSnapshot.getValue(wholePlace.class);
                    wholePlaceList.add(wholePlace);
                }

                ArrayAdapter adapter = new wholePlaceList(ProfileActivity.this, wholePlaceList);
                listViewWholePlace.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showUpdateDialog(final String id, String to, String from){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dBuilder.setView(dialogView);

        final EditText etTo = (EditText) dialogView.findViewById(R.id.etTo);
        final EditText etFrom = (EditText) dialogView.findViewById(R.id.etFrom);
        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        final Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);
        final Button btnAdd = (Button) dialogView.findViewById(R.id.btnAdd);
        dBuilder.setTitle("Updating Start and End Destination");

        etTo.setText(to);
        etFrom.setText(from);

        final AlertDialog aDialog = dBuilder.create();
        aDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String to = etTo.getText().toString().trim();
                String from = etFrom.getText().toString().trim();

                if(to.isEmpty()){
                    etTo.setError("To required.");
                }
                if(from.isEmpty()){
                    etFrom.setError("From required.");
                }

                updateWholePlace(id,to,from);
                aDialog.dismiss();


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                deleteWholePlace(id);
                aDialog.dismiss();



            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                Intent i = new Intent(getApplicationContext(),Contribute2Activity.class);
                i.putExtra(wp_id, id);
                startActivity(i);
                aDialog.dismiss();

            }
        });


    }

    private boolean deleteWholePlace(String id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("wholePlace").child(id);
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("PointToPoint").child(id);

        databaseReference.removeValue();final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("PointToPoint").orderByChild("wp_id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot PointToPoint : dataSnapshot.getChildren()) {
                        PointToPoint.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference2.removeValue();

        Toast.makeText(this,id, Toast.LENGTH_SHORT).show();
        return true;

    }

    private  boolean updateWholePlace(String id, String to, String from){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("wholePlace").child(id);
        String id2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        wholePlace wPlace = new wholePlace(from, to, id2, id);
        databaseReference.setValue(wPlace);
        Toast.makeText(this,"Record Updated Successfully!", Toast.LENGTH_SHORT).show();
        return true;
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


        //list view
//        databaseWholeList.addValueEventListener(new ValueEventListener() {

        //choose


        if (mAuth.getCurrentUser() == null) {
            //handle already login
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    public void toContribute1(View v) {
        Intent i = new Intent(this, Contribute1Activity.class);
        startActivity(i);
    }

    public void toSeeLatest(View v) {
        Intent i = new Intent(this, SeeLatestActivity.class);
        startActivity(i);
    }

    public void toAll(View v) {
        Intent i = new Intent(this, allpage.class);
        startActivity(i);
    }


}
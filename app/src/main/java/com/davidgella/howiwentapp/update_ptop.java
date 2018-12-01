package com.davidgella.howiwentapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class update_ptop extends AppCompatActivity implements View.OnClickListener {
    TextView tvUsername, tvFrom2, tvTo2;
    FirebaseAuth mAuth;
    ListView listViewWholePlace;
    List<PointToPoint>  PointToPointList;
    public static final String P2P_ID = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_ptop);

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

        listViewWholePlace.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PointToPoint p2p = PointToPointList.get(position);
                showUpdateDialog(p2p.getKey(),p2p.getTo(),p2p.getFrom(),p2p.getMot(),p2p.getFare(),p2p.getComment(),p2p.getWp_id());
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

                ArrayAdapter adapter = new PointToPointList(update_ptop.this, PointToPointList);
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

    private void showUpdateDialog(final String id, String to, String from, String mot, String fare, String comment, final String wp_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog2,null);

        dBuilder.setView(dialogView);

        final EditText etTo = (EditText) dialogView.findViewById(R.id.etTo);
        final EditText etFrom = (EditText) dialogView.findViewById(R.id.etFrom);
        final EditText etFare = (EditText) dialogView.findViewById(R.id.etFare);
        final EditText etComment = (EditText) dialogView.findViewById(R.id.etComment);
        final Spinner sMOT = (Spinner) dialogView.findViewById(R.id.sMOT);

        etTo.setText(to);
        etFrom.setText(from);
        etFare.setText(fare);
        etComment.setText(comment);





        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        final Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);
        dBuilder.setTitle("Updating Start and End Sub Destination");

        final AlertDialog aDialog = dBuilder.create();
        aDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String from = etFrom.getText().toString().trim();
                String to = etTo.getText().toString().trim();
                String comment = etComment.getText().toString();
                String fare = etFare.getText().toString();
                String mot = sMOT.getSelectedItem().toString();

                if(to.isEmpty()){
                    etTo.setError("To required.");
                }
                if(from.isEmpty()){
                    etFrom.setError("From required.");
                }

                if(fare.isEmpty()){
                    etFare.setError("Fare required.");
                }

                updateWholePlace(from, to, mot, fare, comment, wp_id, id);
                aDialog.dismiss();


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                deleteP2P(id);
                aDialog.dismiss();



            }
        });


    }

        private boolean deleteP2P(String id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PointToPoint").child(id);
        databaseReference.removeValue();

        Toast.makeText(this,"Record deleted!", Toast.LENGTH_SHORT).show();
        return true;

    }




        private  boolean updateWholePlace(String from, String to, String mot, String fare, String comment, String wp_id, String key){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PointToPoint").child(key);
        String id2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        PointToPoint  p2p = new PointToPoint(from, to, mot, fare, comment, wp_id, key);
        databaseReference.setValue(p2p);
        Toast.makeText(this,"Record Updated Successfully!", Toast.LENGTH_SHORT).show();
        return true;
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

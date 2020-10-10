package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SeekerSideConfirmedReqView extends AppCompatActivity {

    Button btnJobCompleted;
    Button btnViewRequest;
    DatabaseReference dR;
    String orderId;
    String userID;
    SharedPreferences sharedPreferences ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_side_confirmed_req_view);
        btnJobCompleted = findViewById(R.id.btnJobComplete);
        btnViewRequest = findViewById(R.id.btnViewRequest);

        sharedPreferences = getSharedPreferences("seekerlistclick", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        userID  =  FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        orderId = sharedPreferences.getString("OrderId",null);
        Log.i("order id ki value ",orderId);

        btnJobCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dR = FirebaseDatabase.getInstance().getReference("RequestsSeeker");

                dR.child(userID).child(orderId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String recruiterID = dataSnapshot.child("recruiterId").getValue().toString();

                        Map<String,Object> status = new HashMap<>();
                        status.put("statusCode","3");
                        FirebaseDatabase.getInstance().getReference("RequestsRecruiter")
                                .child(recruiterID)
                                .child(orderId)
                                .updateChildren(status);
                        FirebaseDatabase.getInstance().getReference("RequestsSeeker")
                                .child(userID)
                                .child(orderId)
                                .updateChildren(status);
                        Intent intent = new Intent( SeekerSideConfirmedReqView.this,DrawerActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        btnViewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( SeekerSideConfirmedReqView.this,SeekerSideReqView.class);
                startActivity(intent);
            }
        });
    }
}

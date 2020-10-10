package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SharedToUser extends AppCompatActivity {

    CardView cardView,cardView2;
    DatabaseReference dR;
    String userId,orderId;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_to_user);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        sharedPreferences = getSharedPreferences("seekerlistclick", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        orderId = sharedPreferences.getString("OrderId",null);


        cardView =findViewById(R.id.cDRecruiterShare);
//        cardView2 = findViewById(R.id.cDRecruiterWorkCompleted);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SharedToUser.this,SharingActivity.class);
                startActivity(intent);
            }
        });
//        cardView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dR = FirebaseDatabase.getInstance().getReference("RequestsSeeker");
//                dR.child(userId).child(orderId).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        String recruiterID = dataSnapshot.child("recruiterId").getValue().toString();
//
//                        Map<String,Object> status = new HashMap<>();
//                        status.put("statusCode","2");
//                        FirebaseDatabase.getInstance().getReference("RequestsRecruiter")
//                                .child(recruiterID)
//                                .child(orderId)
//                                .updateChildren(status);
//                        FirebaseDatabase.getInstance().getReference("RequestsSeeker")
//                                .child(userId)
//                                .child(orderId)
//                                .updateChildren(status);
//                        Intent intent = new Intent( SharedToUser.this,DrawerActivity.class);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
    }
}

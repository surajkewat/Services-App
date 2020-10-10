package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SeekerProfileView extends AppCompatActivity {

    String userId;
    DatabaseReference dR,dR2;
    TextView t1,t2,t3,t4,t5,t6,t7;
    ImageView imageView;
    Button btn;
    String seekerId;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_profile_view);
        t1 = findViewById(R.id.txtProfileNameViewXT);
        t2 = findViewById(R.id.txtProfileEmailViewXT);
        t3 =findViewById(R.id.txtProfileContactViewXT);
        t4 = findViewById(R.id.txtProfileGenderViewXT);
        t5 = findViewById(R.id.txtProfileProfessionViewXT);
        t6 = findViewById(R.id.txtProfileStateViewXT);
        t7 = findViewById(R.id.txtProfileCityViewXT);

        btn=findViewById(R.id.btnSendReqXT);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeekerProfileView.this,PostRequest.class);
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.profileImageViewXT);
        sharedPreferences = getSharedPreferences("Details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        seekerId = sharedPreferences.getString("id",null);
        dR = FirebaseDatabase.getInstance().getReference("person");
        dR.child(seekerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("imgUrl").getValue().toString()).into(imageView);
                t1.setText("Name : "+dataSnapshot.child("name").getValue().toString());
                t3.setText("Contact : "+dataSnapshot.child("contact").getValue().toString());
                t5.setText("Profession : "+dataSnapshot.child("profession").getValue().toString());
                t6.setText("State : "+dataSnapshot.child("state").getValue().toString());
                t2.setText("E-mail : "+dataSnapshot.child("email").getValue().toString());
                t4.setText("Gender : "+dataSnapshot.child("gender").getValue().toString());
                t7.setText("City : "+dataSnapshot.child("city").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

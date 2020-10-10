package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentDOne extends AppCompatActivity {
    DatabaseReference dR;
    SharedPreferences sharedPreferences;
    Button btn;
    String recuiterName;
    String amount,orderId,userId;
    TextView txtInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_done);
        btn = findViewById(R.id.btnPaymentNotDone);
        txtInfo = findViewById(R.id.txtPaymentInfo);

        sharedPreferences = getSharedPreferences("seekerlistclick", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        orderId = sharedPreferences.getString("OrderId",null);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        dR = FirebaseDatabase.getInstance().getReference("RequestsSeeker");
        dR.child(userId).child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recuiterName = dataSnapshot.child("recruitername").getValue().toString();
                amount = dataSnapshot.child("amount").getValue().toString();
                txtInfo.setText(amount+" by "+recuiterName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentDOne.this,DrawerActivity.class);
                startActivity(intent);

            }
        });
    }
}

package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SharingActivity extends AppCompatActivity {

    Button btn;
    EditText txt;
    DatabaseReference dR;
    String sharedId;
    SharedPreferences sharedPreferences,sharedPreferences2 ;
    String orderId;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);


        btn= findViewById(R.id.btn_ShareRequest);
        txt= findViewById(R.id.txtShareContact);



        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

//        sharedPreferences = getSharedPreferences("recruiterlistclick", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        orderId = sharedPreferences.getString("OrderId",null);
        orderId="-M5Zxyt9WYmST4Y0-6aa";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference("person");
                dr1.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String senderName = dataSnapshot.child("name").getValue().toString();
                        final String senderId = dataSnapshot.child("personId").getValue().toString();
                        DatabaseReference dr2 = FirebaseDatabase.getInstance().getReference("contacts");
                        dr2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.i("Values",senderName+senderId);
                                Log.i("receiver id",dataSnapshot.child(txt.getText().toString()).getValue().toString());

                                final String sharedId = dataSnapshot.child(txt.getText().toString()).getValue().toString();
                                DatabaseReference dr3 = FirebaseDatabase.getInstance().getReference("RequestsRecruiter");
                                dr3.child(userId).child(orderId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String imageUrl = dataSnapshot.child("imgUrl").getValue().toString();
                                        String seekerId = dataSnapshot.child("seekerId").getValue().toString();
                                        String txtSpec = dataSnapshot.child("txtSpec").getValue().toString();
                                        String vidUrl = dataSnapshot.child("vidUrl").getValue().toString();
                                        String subCat = dataSnapshot.child("subCat").getValue().toString();
                                        String statusCode = dataSnapshot.child("statusCode").getValue().toString();
                                        String paymentStatus = dataSnapshot.child("paymentStatus").getValue().toString();
                                        String amount = dataSnapshot.child("amount").getValue().toString();

                                        DatabaseReference dr4 = FirebaseDatabase.getInstance().getReference("SharedRequests");
                                        ShareRequests shareRequests = new ShareRequests(senderName,senderId,orderId,imageUrl,seekerId,txtSpec,vidUrl,subCat,statusCode,paymentStatus,amount);
                                        dr4.child(sharedId).child(orderId).setValue(shareRequests).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SharingActivity.this,"Shared Successfully",Toast.LENGTH_SHORT).show();
                                                PackageManager pm = SharingActivity.this.getPackageManager();
                                                try {
                                                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                                                    waIntent.setType("text/plain");
                                                    String text = "https://suberservicesapp.000webhostapp.com/?data="+orderId;
                                                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                                                    waIntent.setPackage("com.whatsapp");
                                                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                                                    startActivity(Intent.createChooser(waIntent, "Share with"));
                                                } catch (PackageManager.NameNotFoundException e) {
                                                    Toast.makeText(SharingActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });
    }
}

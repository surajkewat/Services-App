package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class WhatsAppSend extends AppCompatActivity {

    EditText textView;
    Button btn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_send);

        textView = findViewById(R.id.txtShareNumber);
        btn = findViewById(R.id.btnWhatsapp);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNumber = textView.getText().toString();
                String userId  = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("person");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                            if(phoneNumber.equals(dataSnapshot1.child("contact").getValue())){
                                PackageManager pm = WhatsAppSend.this.getPackageManager();
                                try {
                                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                                    waIntent.setType("text/plain");
                                    String text = "https://suberservicesapp.000webhostapp.com/?data=1234";
                                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                                    waIntent.setPackage("com.whatsapp");
                                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                                    startActivity(Intent.createChooser(waIntent, "Share with"));
                                } catch (PackageManager.NameNotFoundException e) {
                                    Toast.makeText(WhatsAppSend.this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                                            .show();
                                }
//                            }
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}

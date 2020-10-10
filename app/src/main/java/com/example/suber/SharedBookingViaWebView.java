package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class SharedBookingViaWebView extends AppCompatActivity {

    String userId,orderId;
    TextView txt1,txt2,seekerName,seekerContact;
    ImageView imageView,seerkerImage;
    VideoView videoView;
    DatabaseReference databaseReference,dR;

    SharedPreferences sharedPreferences,sharedPreferences2;
    private MediaController mediaController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_booking_via_web_view);

        txt1 = findViewById(R.id.txtSubCatSharedWeb);
        txt2 =findViewById(R.id.txtDetailedIssueSharedWeb);
        imageView = findViewById(R.id.imgReqSharedWeb);
        videoView = findViewById(R.id.videoViewSharedWeb);
        seekerName =findViewById(R.id.seekerName);
        seekerContact =findViewById(R.id.seekerContact);
        seerkerImage = findViewById(R.id.imgReqSharedWebSeeker);


        sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        userId = sharedPreferences.getString("ID",null);
//        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if(uri != null){
            orderId = uri.toString();
            orderId = orderId.substring(orderId.indexOf("=")+1 , orderId.length());
            Log.i("Data ",orderId);

        }
        else{
            SharedPreferences sharedPreferences2 = getSharedPreferences("recruiterlistclick", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
            orderId = sharedPreferences2.getString("OrderId",null);
        }


        Log.i("User Id :",userId);
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("SharedRequests");
        dr.child(userId).child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String seekerID = dataSnapshot.child("seekerId").getValue().toString();


                Picasso.get().load(dataSnapshot.child("imgUrl").getValue().toString()).into(imageView);
                txt1.setText(dataSnapshot.child("subCat").getValue().toString());
                txt2.setText(dataSnapshot.child("txtSpec").getValue().toString());
                Log.i("url",dataSnapshot.child("vidUrl").getValue().toString());


                try {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(dataSnapshot.child("vidUrl").getValue().toString());
                    final File localFile = File.createTempFile("userReq","3gp");
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            videoView.setVideoURI(Uri.fromFile(localFile));
                            mediaController = new MediaController(SharedBookingViaWebView.this);
                            mediaController.setAnchorView(videoView);
                            videoView.setMediaController(mediaController);
                            videoView.start();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                }
                catch (IOException e) {
                    Toast.makeText(SharedBookingViaWebView.this,"Failed to create temp file "+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                dR = FirebaseDatabase.getInstance().getReference("person");
                dR.child(seekerID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Picasso.get().load(dataSnapshot.child("imgUrl").getValue().toString()).into(seerkerImage);
                        seekerName.setText(dataSnapshot.child("name").getValue().toString());
                        seekerContact.setText(dataSnapshot.child("contact").getValue().toString());
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SharedBookingViaWebView.this,login_form.class);
        startActivity(intent);
    }
}

package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SeekerSideReqView extends AppCompatActivity {
    TextView txt1,txt2;
    ImageView imageView;
    VideoView videoView;
    DatabaseReference databaseReference;
    DatabaseReference dR;
    private MediaController mediaController;
    Button btnAcceptReq;
    String orderId;



    String userID;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_side_req_view);

        txt1 = findViewById(R.id.txtSubCat);
        txt2 =findViewById(R.id.txtDetailedIssue);
        imageView = findViewById(R.id.imgReqSeekerSide);
        videoView = findViewById(R.id.videoView2);
        btnAcceptReq = findViewById(R.id.btnAcceptReq);

        sharedPreferences = this.getSharedPreferences("seekerlistclick", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        userID  =  FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("RequestsSeeker");
        orderId = sharedPreferences.getString("OrderId",null);
        Log.i("OrderID",sharedPreferences.getString("OrderId",null));
        databaseReference.child(userID).child(sharedPreferences.getString("OrderId",null)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                            mediaController = new MediaController(SeekerSideReqView.this);
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
                    Toast.makeText(SeekerSideReqView.this,"Failed to create temp file "+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAcceptReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dR = FirebaseDatabase.getInstance().getReference("RequestsSeeker");
                dR.child(userID).child(orderId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String recruiterID = dataSnapshot.child("recruiterId").getValue().toString();

                        Map<String,Object> status = new HashMap<>();
                        status.put("statusCode","2");
                        FirebaseDatabase.getInstance().getReference("RequestsRecruiter")
                                .child(recruiterID)
                                .child(orderId)
                                .updateChildren(status);
                        FirebaseDatabase.getInstance().getReference("RequestsSeeker")
                                .child(userID)
                                .child(orderId)
                                .updateChildren(status);
                        Intent intent = new Intent( SeekerSideReqView.this,DrawerActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}

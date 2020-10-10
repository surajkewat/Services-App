package com.example.suber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeekerConfirmedRequests extends AppCompatActivity implements RequestsAdapter.OnRequestClickListner {

    Button btn;
    RecyclerView recyclerView;
    DatabaseReference dr;
    private  ArrayList<RequestsSeeker> uploads=new ArrayList<RequestsSeeker>();
    int state=0;
    TextView txt;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_confirmed_requests);

        btn=(Button)findViewById(R.id.btnSeekerConfirmed);
        txt = findViewById(R.id.txtNoConfirmedReqSeeker);

        recyclerView = (RecyclerView) findViewById(R.id.seeker_confirmed_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


//        sharedPreferences = this.getSharedPreferences("address", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();

        final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        dr= FirebaseDatabase.getInstance().getReference("RequestsSeeker");
        dr.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    uploads=new ArrayList<RequestsSeeker>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Log.i("values",dataSnapshot.getValue().toString());
                        RequestsSeeker u = postSnapshot.getValue(RequestsSeeker.class);
                        if(u.statusCode.equals("2")){
                            uploads.add(u);
                            txt.setVisibility(View.GONE);
                        }
                    }
                    load(uploads);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RequestsSeeker u = dataSnapshot.getValue(RequestsSeeker.class);
                load(uploads);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void load(ArrayList<RequestsSeeker> uploads){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        RequestsAdapter adapter = new RequestsAdapter(uploads,this);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestClick(int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("seekerlistclick", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("OrderId",uploads.get(position).orderId);
        editor.commit();
        Intent intent= new Intent(this,SeekerSideConfirmedReqView.class);
        startActivity(intent);
    }
}

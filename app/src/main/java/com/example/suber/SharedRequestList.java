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

public class SharedRequestList extends AppCompatActivity implements SharedRequestAdapter.OnRequestClickListner  {

    Button btn;
    TextView txt;
    RecyclerView recyclerView;
    DatabaseReference dr;
    ArrayList<ShareRequests> uploads=new ArrayList<ShareRequests>();
    int state=0;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_request_list);

        btn=(Button)findViewById(R.id.btnRecruiterPending);

        recyclerView = (RecyclerView) findViewById(R.id.recuiter_shared_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        dr= FirebaseDatabase.getInstance().getReference("SharedRequests");
        dr.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploads=new ArrayList<ShareRequests>();
                Log.i("Above values","anaskdnsakdna");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.i("values",dataSnapshot.getValue().toString());
                    ShareRequests u = postSnapshot.getValue(ShareRequests.class);
                    uploads.add(u);
                    txt.setVisibility(View.GONE);
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
                ShareRequests u = dataSnapshot.getValue(ShareRequests.class);
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
    void load(ArrayList<ShareRequests> uploads){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        SharedRequestAdapter adapter = new SharedRequestAdapter(uploads,this);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestClick(int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("recruiterlistclick", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("OrderId",uploads.get(position).orderId);
        editor.commit();
        Intent intent= new Intent(this,SharedBookingViaWebView.class);
        startActivity(intent);
    }
}


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

public class RecruiterCompletedRequests extends AppCompatActivity implements RecruiterRequestsAdapter.OnRequestClickListner{

    Button btn;
    RecyclerView recyclerView;
    DatabaseReference dr;
    ArrayList<RecruiterRequests> uploads=new ArrayList<RecruiterRequests>();
    int state=0;
    SharedPreferences sharedPreferences ;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_completed_requests);

        btn=(Button)findViewById(R.id.btnRecruiterCompleted);
        txt  = findViewById(R.id.txtNoCompletedReqRecruiter);

        recyclerView = (RecyclerView) findViewById(R.id.recuiter_completed_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        dr= FirebaseDatabase.getInstance().getReference("RequestsRecruiter");
        dr.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploads=new ArrayList<RecruiterRequests>();
                Log.i("Above values","anaskdnsakdna");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.i("values",dataSnapshot.getValue().toString());
                    RecruiterRequests u = postSnapshot.getValue(RecruiterRequests.class);
                    if(u.statusCode.equals("3")){
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
                RecruiterRequests u = dataSnapshot.getValue(RecruiterRequests.class);
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
    void load(ArrayList<RecruiterRequests> uploads){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        RecruiterRequestsAdapter adapter = new RecruiterRequestsAdapter(uploads,this);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestClick(int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("completeclick", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("OrderId",uploads.get(position).orderId);
        editor.putString("seekerId",uploads.get(position).seekerId);
        editor.commit();

        Intent intent= new Intent(this,PaymentFromRecruiter.class);
        startActivity(intent);
    }
}

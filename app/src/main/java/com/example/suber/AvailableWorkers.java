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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.FutureTask;

public class AvailableWorkers extends AppCompatActivity implements PersonAdaptor.OnWorkerClickListner {

    Button btn;
    RecyclerView recyclerView;
    DatabaseReference dr;
    ArrayList<Person> uploads=new ArrayList<Person>();
    int state=0;
    SharedPreferences sharedPreferences,sharedPreferences2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_workers);

        btn=(Button)findViewById(R.id.btn);

        recyclerView = (RecyclerView) findViewById(R.id.person_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        sharedPreferences2 = this.getSharedPreferences("categories" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();

        sharedPreferences = this.getSharedPreferences("address", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        final String Category = sharedPreferences2.getString("category",null);

        dr= FirebaseDatabase.getInstance().getReference("person");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploads=new ArrayList<Person>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Person u = postSnapshot.getValue(Person.class);
                    String k = u.getCity();
                    Log.i("Person: Shared",u.getCity()+" : "+sharedPreferences.getString("City",null));
                    if(k.equals(sharedPreferences.getString("City",null))) {
                        Log.i("Person: Shared",u.getCity()+" : "+sharedPreferences.getString("City",null));
                        if(u.personCode.equals("2") && u.profession.equals(Category) ){
                            uploads.add(u);
                        }
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
                Person u = dataSnapshot.getValue(Person.class);
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

    void load(ArrayList<Person> uploads){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        PersonAdaptor adapter = new PersonAdaptor(uploads,this);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onWorkerClick(int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("Details",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name",uploads.get(position).name);
        editor.putString("id",uploads.get(position).personId);
        editor.commit();
        Intent intent= new Intent(this,SeekerProfileView.class);
        startActivity(intent);
    }
}

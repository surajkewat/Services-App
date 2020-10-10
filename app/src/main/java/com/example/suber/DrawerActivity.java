package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView profilePic;
    StorageReference storageReference;
    DatabaseReference mRef;
    TextView txtName;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        drawerLayout = findViewById(R.id.drawerID);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ////////////new af
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().clear();

        sharedPreferences = this.getSharedPreferences("userDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String k = sharedPreferences.getString("user",null);


        if(k.equals("seeker")){
            navigationView.inflateMenu(R.menu.menu_seeker);
        }else{
            navigationView.inflateMenu(R.menu.menu_item);
        }

        View hView = navigationView.getHeaderView(0);
        profilePic = hView.findViewById(R.id.profilePic);
        txtName = hView.findViewById(R.id.txtProfileName);



        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        mRef= FirebaseDatabase.getInstance().getReference("person");
        mRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("imgUrl").getValue().toString()).into(profilePic);
                txtName.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef= FirebaseDatabase.getInstance().getReference("person");
        mRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String userCode = dataSnapshot.child("personCode").getValue().toString() ;

                    Log.i("Usercode",userCode);

                    if(userCode.equals("2")){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Seeker_Requests_Fragment()).commit();
                    }
                    else if(userCode.equals("1")) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RecuriterCatFrag()).commit();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Profile_Fragment()).commit();
                Toast.makeText(DrawerActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.order:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RecuriterCatFrag()).commit();
                Toast.makeText(DrawerActivity.this, "Order", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.logout:
                Toast.makeText(DrawerActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.share:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ShareFragment()).commit();
                Toast.makeText(DrawerActivity.this, "Share", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.requests:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Recruiter_Requets_Frag()).commit();
                Toast.makeText(DrawerActivity.this, "Requests", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.profileSeeker:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Profile_Fragment()).commit();
                Toast.makeText(DrawerActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.logoutSeeker:
                Toast.makeText(DrawerActivity.this, "logout", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.requestSeeker:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Seeker_Requests_Fragment()).commit();
                Toast.makeText(DrawerActivity.this, "Requests", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            default:
                break;
        }
        return false;
    }



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }
}

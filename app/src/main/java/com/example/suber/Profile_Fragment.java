package com.example.suber;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile_Fragment extends Fragment {

    String userId;
    DatabaseReference dR,dR2;
    TextView t1,t2,t3,t4,t5,t6,t7;
    ImageView imageView;
    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        t1 = view.findViewById(R.id.txtProfileNameView);
        t2 = view.findViewById(R.id.txtProfileEmailView);
        t3 = view.findViewById(R.id.txtProfileContactView);
        t4 = view.findViewById(R.id.txtProfileGenderView);
        t5 = view.findViewById(R.id.txtProfileProfessionView);
        t6 = view.findViewById(R.id.txtProfileStateView);
        t7 = view.findViewById(R.id.txtProfileCityView);

        btn= view.findViewById(R.id.btnGoToEditProfile);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditProfile.class);
                startActivity(intent);
            }
        });

        imageView = view.findViewById(R.id.profileImageView);


        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        dR = FirebaseDatabase.getInstance().getReference("person");
        dR.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("imgUrl").getValue().toString()).into(imageView);
                t1.setText("Name : "+dataSnapshot.child("name").getValue().toString());
                t3.setText("Contact : "+dataSnapshot.child("contact").getValue().toString());
                t5.setText("Profession : "+dataSnapshot.child("profession").getValue().toString());
                t6.setText("State : "+dataSnapshot.child("state").getValue().toString());
                t2.setText("E-mail : "+dataSnapshot.child("email").getValue().toString());
                t4.setText("Gender : "+dataSnapshot.child("gender").getValue().toString());
                t7.setText("City : "+dataSnapshot.child("city").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        dR2 = FirebaseDatabase.getInstance().getReference("users");
//        dR2.child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                t2.setText("E-mail : "+dataSnapshot.child("email").getValue().toString());
//                t4.setText("Gender : "+dataSnapshot.child("gender").getValue().toString());
//                t7.setText("City : "+dataSnapshot.child("city").getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        return view;
    }
}

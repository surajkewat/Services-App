package com.example.suber;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class Seeker_Requests_Fragment  extends  Fragment {
    CardView cardView1,cardView2,cardView3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seeker_requests,container,false);

        cardView1 =view.findViewById(R.id.cDSeekerPending);
        cardView2 =view.findViewById(R.id.cDSeekerConfirmed);
        cardView3 =view.findViewById(R.id.cDSeekerCompleted);


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(),"Seeker Pending",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getActivity(),SeekerPendingRequests.class);
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),SeekerConfirmedRequests.class);
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),SeekerCompletedRequests.class);
                startActivity(intent);
            }
        });

        return view;
    }
}

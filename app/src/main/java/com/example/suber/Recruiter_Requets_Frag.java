package com.example.suber;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class Recruiter_Requets_Frag extends Fragment {
    CardView cardView1,cardView2,cardView3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruiter_requests,container,false);

        cardView1 =view.findViewById(R.id.cDRecruiterPending);
        cardView2 =view.findViewById(R.id.cDRecruiterConfirmed);
        cardView3 =view.findViewById(R.id.cDRecruiterCompleted);


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getActivity(),RecruiterPendingRequests.class);
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),RecruiterConfirmedRequests.class);
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),RecruiterCompletedRequests.class);
                startActivity(intent);
            }
        });

        return view;
    }
}

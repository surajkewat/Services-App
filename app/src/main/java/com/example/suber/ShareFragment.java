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

public class ShareFragment extends Fragment {
    CardView c1,c2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share,container,false);
//        c1 = view.findViewById(R.id.cDSharedByYou);
        c2 = view.findViewById(R.id.cDSharedToYou);
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SharedRequestList.class);
                startActivity(intent);
            }
        });
        return view;
    }
}

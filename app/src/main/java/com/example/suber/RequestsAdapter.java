package com.example.suber;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.Holder>  {

    Context context;
    ArrayList<RequestsSeeker> data;
    private OnRequestClickListner monRequestClickListner;

    public RequestsAdapter (ArrayList<RequestsSeeker> data,OnRequestClickListner onRequestClickListner)
    {
        this.data = data;
        this.monRequestClickListner= onRequestClickListner;
    }



    RequestsAdapter(Context context, ArrayList data){
        this.context=context;
        this.data=data;
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView contact;
        TextView profession;
        TextView city;
        TextView state;
        ImageView img;
        OnRequestClickListner onRequestClickListner;
//        LinearLayout linearLayout;

        public Holder(View v,OnRequestClickListner onRequestClickListner) {
            super(v);

            name=v.findViewById(R.id.fname);
            contact = v.findViewById(R.id.fcontact);
            profession =v.findViewById(R.id.fprofession);
//            city = v.findViewById(R.id.fCity);
//            state = v.findViewById(R.id.fState);
            img=v.findViewById(R.id.personImage);
//            linearLayout = v.findViewById(R.id.linear_layout);
            this.onRequestClickListner = onRequestClickListner;
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onRequestClickListner.onRequestClick(getAdapterPosition());

        }
    }
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.available_worker_layout, parent, false);
        return new Holder(v,monRequestClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.name.setText("Name: "+data.get(position).getRecruitername());
        holder.profession.setText("Problem : "+data.get(position).getSubCat());
        holder.contact.setText("Status : "+data.get(position).getStatusCode());
//        holder.city.setText("City: "+data.get(position).getCity());
//        holder.state.setText("Email: "+data.get(position).getState());

//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,PostRequest.class);
//                context.startActivity(intent);
//            }
//        });
        Log.v("url",data.get(position).getImgUrl());
        Picasso.get()
                .load(data.get(position).getImgUrl())
                .into(holder.img);
        if(position==data.size()-1){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(0, 0, 0, 250);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnRequestClickListner{
        void onRequestClick(int position);
    }

}

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


public class PersonAdaptor extends RecyclerView.Adapter<PersonAdaptor.Holder>  {

    Context context;
    ArrayList<Person> data;
    private OnWorkerClickListner monWorkerClickListner;

    public PersonAdaptor(ArrayList<Person> data,OnWorkerClickListner onWorkerClickListner)
    {
        this.data = data;
        this.monWorkerClickListner= onWorkerClickListner;
    }



   PersonAdaptor(Context context, ArrayList data){
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
        OnWorkerClickListner onWorkerClickListner;
//        LinearLayout linearLayout;

        public Holder(View v,OnWorkerClickListner onWorkerClickListner) {
            super(v);

            name=v.findViewById(R.id.fname);
            contact = v.findViewById(R.id.fcontact);
            profession =v.findViewById(R.id.fprofession);
//            city = v.findViewById(R.id.fCity);
//            state = v.findViewById(R.id.fState);
            img=v.findViewById(R.id.personImage);
//            linearLayout = v.findViewById(R.id.linear_layout);
            this.onWorkerClickListner = onWorkerClickListner;
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onWorkerClickListner.onWorkerClick(getAdapterPosition());

        }
    }
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.available_worker_layout, parent, false);
        return new Holder(v,monWorkerClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.name.setText("Name: "+data.get(position).getName());
        holder.profession.setText("Profssion : "+data.get(position).getProfession());
        holder.contact.setText("Contact : "+data.get(position).getContact());
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

    public interface OnWorkerClickListner{
        void onWorkerClick(int position);
    }

}

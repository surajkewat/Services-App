package com.example.suber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecuriterCatFrag extends Fragment {


    ImageView i1,i2,i3,i4,i5,i6;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recuiter_catergories,container,false);
        i1 = view.findViewById(R.id.electrician);
        i2 = view.findViewById(R.id.bricklayer);

        i3 = view.findViewById(R.id.plumber);
        i4 = view.findViewById(R.id.labour);
        i5 = view.findViewById(R.id.carpainter);
        i6 = view.findViewById(R.id.painter);

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("categories" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String categorie;
                String img_var;
                categorie = "Electrician";
                editor.putString("category" , categorie);
                editor.commit();
                doThis();
//                Toast.makeText(getContext(),"Electrician",Toast.LENGTH_SHORT).show();
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("categories" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String categorie;
                String img_var;
                categorie = "Mason";
                editor.putString("category" , categorie);
                editor.commit();
                doThis();
//                Toast.makeText(getContext(),"Electrician",Toast.LENGTH_SHORT).show();
            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("categories" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String categorie;
                String img_var;
                categorie = "Plumber";
                editor.putString("category" , categorie);
                editor.commit();
                doThis();
//                Toast.makeText(getContext(),"Electrician",Toast.LENGTH_SHORT).show();
            }
        });
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("categories" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String categorie;
                String img_var;
                categorie = "Labour";
                editor.putString("category" , categorie);
                editor.commit();
                doThis();
//                Toast.makeText(getContext(),"Electrician",Toast.LENGTH_SHORT).show();
            }
        });
        i5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("categories" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String categorie;
                String img_var;
                categorie = "Carpenter";
                editor.putString("category" , categorie);
                editor.commit();
                doThis();
//                Toast.makeText(getContext(),"Electrician",Toast.LENGTH_SHORT).show();
            }
        });
        i6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("categories" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String categorie;
                String img_var;
                categorie = "Painter";
                editor.putString("category" , categorie);
                editor.commit();
                doThis();
//                Toast.makeText(getContext(),"Electrician",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void doThis(){
        Intent intent= new Intent(getActivity(),GeoLocation.class);
        startActivity(intent);
    }

//    public void dothis(View v)
//    {
//        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Categories" , Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        String categorie;
//        String img_var;
//        Intent intent;
//        switch(v.getId())
//        {
//
//            case R.id.electrician:
//                categorie = "Electrician";
//                editor.putString("categorie" , categorie);
//                editor.commit();
//                //Toast.makeText(RecruiterMain.this , "Electrician" , Toast.LENGTH_SHORT).show();
//                intent = new Intent(RecuriterCatFrag.this.getActivity() , GeoLocation.class);
//                startActivity(intent);
//                break;
//
//            case R.id.bricklayer:
//                Toast.makeText(getContext() , "Mason" , Toast.LENGTH_SHORT).show();
//                categorie="Mason";
//                editor.putString("categorie",categorie);
//                editor.commit();
//                intent=new Intent(RecuriterCatFrag.this.getActivity(),GeoLocation.class);
//                startActivity(intent);
//                break;
//            case R.id.carpainter:
//                Toast.makeText(getContext() , "Carpainter" , Toast.LENGTH_SHORT).show();
//                categorie = "Carpenter";
//                editor.putString("categorie" , categorie);
//                editor.commit();
//                intent = new Intent(RecuriterCatFrag.this.getActivity() , GeoLocation.class);
//                startActivity(intent);
//                break;
//            case R.id.plumber:
//                Toast.makeText(getContext() , "Plumber" , Toast.LENGTH_SHORT).show();
//                categorie="Plumber";
//                editor.putString("categorie",categorie);
//                editor.commit();
//                intent=new Intent(RecuriterCatFrag.this.getActivity(),GeoLocation.class);
//                startActivity(intent);
//                break;
//            case R.id.painter:
//                Toast.makeText(getContext() , "Painter" , Toast.LENGTH_SHORT).show();
//                categorie="Painter";
//                editor.putString("categorie",categorie);
//                editor.commit();
//                intent=new Intent(RecuriterCatFrag.this.getActivity(),GeoLocation.class);
//                startActivity(intent);
//                break;
//            case R.id.labour:
//                Toast.makeText(getContext() , "Labour" , Toast.LENGTH_SHORT).show();
//                categorie="Labour";
//                editor.putString("categorie",categorie);
//                editor.commit();
//                intent=new Intent(RecuriterCatFrag.this.getActivity(),GeoLocation.class);
//                startActivity(intent);
//                break;
//            default:
//                break;
//        }
//    }
}

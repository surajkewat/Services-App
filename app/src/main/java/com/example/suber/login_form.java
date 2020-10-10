package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class login_form extends AppCompatActivity {

    EditText txtEmail,txtPassword;
    Button btn_login;
    private FirebaseAuth firebaseAuth;
    DatabaseReference mRef,rRef;
    SharedPreferences sharedPreferences,sharedPreferences2;
//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseAuth= FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser()!=null){
//
//            Intent intent = new Intent(this,DrawerActivity.class);
//            startActivity(intent);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        txtEmail = (EditText)findViewById(R.id.txtLoginEmail);
        txtPassword = (EditText)findViewById(R.id.txtLoginPassword);
        btn_login = (Button)findViewById(R.id.btn_login);

        firebaseAuth = FirebaseAuth.getInstance();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password  = txtPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(login_form.this,"Please Enter Email-id",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(login_form.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login_form.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    String userId =FirebaseAuth.getInstance().getCurrentUser().getUid().toString();


                                    rRef= FirebaseDatabase.getInstance().getReference("person");
                                    rRef.child(userId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String userCode = dataSnapshot.child("personCode").getValue().toString() ;


                                            sharedPreferences2= getSharedPreferences("userId", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                            editor2.putString("ID",dataSnapshot.child("personId").getValue().toString());
                                            editor2.putString("userName",dataSnapshot.child("name").getValue().toString());
                                            editor2.commit();

                                            sharedPreferences = getSharedPreferences("userDetail", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            Log.i("NikaL",userCode);

                                            if(userCode.equals("2")){
                                                editor.putString("user","seeker");
                                                editor.commit();
                                            }
                                            else if(userCode.equals("1")) {
                                                editor.putString("user","recruiter");
                                                editor.commit();
                                            }
                                            startActivity(new Intent(getApplicationContext(),DrawerActivity.class));
                                            Toast.makeText(login_form.this,"Login Succesfull!!",Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(login_form.this,"Login Failed!!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void btn_SignUpForm(View view) {

        startActivity(new Intent(getApplicationContext(),signUp_form.class));
    }
}


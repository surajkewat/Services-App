package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentFromRecruiter extends AppCompatActivity {

    EditText amountEt, noteEt, nameEt, upiIdEt;
    Button send;
    final int UPI_PAYMENT = 0;
    DatabaseReference dR;
    String orderId;
    String userId,seekerId,userName;
    SharedPreferences sharedPreferences,sharedPreferences2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_from_recruiter);



        send = findViewById(R.id.send);
        amountEt = findViewById(R.id.amount_et);
        noteEt = findViewById(R.id.note);
        nameEt = findViewById(R.id.name);
        upiIdEt = findViewById(R.id.upi_id);
        sharedPreferences = getSharedPreferences("completeclick", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        seekerId  = sharedPreferences.getString("seekerId",null);
        orderId= sharedPreferences.getString("OrderId",null);
        sharedPreferences2= getSharedPreferences("userId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        userName = sharedPreferences2.getString("userName",null);
        Log.i("vals",seekerId+" "+orderId+" "+userName);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the values from the EditTexts
                String amount = amountEt.getText().toString();
                String note = noteEt.getText().toString();
                String name = nameEt.getText().toString();
                String upiId = upiIdEt.getText().toString();

                payUsingUpi(amount, upiId, name, note);


            }
        });



    }

    private void MyMessage() {
        String number = "9869979532";
        String message = "Payment of amount "+amountEt.getText().toString()+" received by "+userName;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number,null,message,null,null);

        Toast.makeText(this, "message sent", Toast.LENGTH_SHORT).show();

    }

    void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PaymentFromRecruiter.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 0:

                if (grantResults.length >=0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    MyMessage();
                }
                else {
                    Toast.makeText(PaymentFromRecruiter.this,"Please allow SMS permission",Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PaymentFromRecruiter.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PaymentFromRecruiter.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
                userId =  FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                dR = FirebaseDatabase.getInstance().getReference("RequestsRecruiter");
                dR.child(userId).child(orderId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        int permissionCheck = ContextCompat.checkSelfPermission(PaymentFromRecruiter.this,Manifest.permission.SEND_SMS);
                        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                            MyMessage();
                        }
                        else {
                            ActivityCompat.requestPermissions(PaymentFromRecruiter.this,new String[]{Manifest.permission.SEND_SMS},0);
                        }
//                        String seekerID = dataSnapshot.child("seekerId").getValue().toString();

                        Map<String,Object> status = new HashMap<>();
                        status.put("paymentStatus","1");
                        Map<String,Object> amount = new HashMap<>();
                        amount.put("amount",amountEt.getText().toString());
                        FirebaseDatabase.getInstance().getReference("RequestsRecruiter")
                                .child(userId)
                                .child(orderId)
                                .updateChildren(status);
                        FirebaseDatabase.getInstance().getReference("RequestsSeeker")
                                .child(seekerId)
                                .child(orderId)
                                .updateChildren(status);
                        FirebaseDatabase.getInstance().getReference("RequestsRecruiter")
                                .child(userId)
                                .child(orderId)
                                .updateChildren(amount);
                        FirebaseDatabase.getInstance().getReference("RequestsSeeker")
                                .child(seekerId)
                                .child(orderId)
                                .updateChildren(amount);


                        Intent intent = new Intent( PaymentFromRecruiter.this,DrawerActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaymentFromRecruiter.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(PaymentFromRecruiter.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentFromRecruiter.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}

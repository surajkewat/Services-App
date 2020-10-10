package com.example.suber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlacePicker;

public class GeoLocation extends AppCompatActivity {

    public static final int REQUEST_CODE_LOCATION_PERMISSION =1;
    TextView txtCords;
    TextView txtAdd;
    ResultReceiver resultReceiver;


    Button curr_location;
    Button another_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_location);

        resultReceiver = new AddressResultReceiver(new Handler());

        txtCords = findViewById(R.id.txtCordinates);
        txtAdd = findViewById(R.id.txtAddress);
        curr_location = findViewById(R.id.curr_Location);
        another_location =findViewById(R.id.other_Location);

        another_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeoLocation.this,MapActivity.class);
                startActivity(intent);
            }
        });

        curr_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(GeoLocation.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_LOCATION_PERMISSION);
                }
                else {
                    getCurrentLocation();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length >0){
            getCurrentLocation();
        }
        else {
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(GeoLocation.this)
                .requestLocationUpdates(locationRequest,new LocationCallback(){

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        LocationServices.getFusedLocationProviderClient(GeoLocation.this)
                                .removeLocationUpdates(this);
                        if(locationResult!= null && locationResult.getLocations().size()>0){
                            int latestLOcationIndex  =locationResult.getLocations().size()-1;
                            double latitude = locationResult.getLocations().get(latestLOcationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLOcationIndex).getLongitude();

//                            txtCords.setText(
//                                    String.format(
//                                            "Latitude: %s\nLongitude: %s",latitude,longitude
//                                    )
//                            );

                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fecthAddressFromLatLong(location);
                        }
                    }
                }, Looper.getMainLooper());

    }

    private void fecthAddressFromLatLong(Location location){

        Intent intent = new Intent(this,FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER,resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver{

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == Constants.SUCCESS_RESULT){
//                txtAdd.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                SharedPreferences sharedPreferences = getSharedPreferences("address",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("City",resultData.getString("City"));
                editor.putString("Address",resultData.getString("Address"));
                editor.commit();
                Log.i("Info : ",sharedPreferences.getString("City",null));
                Intent intent=new Intent(getApplicationContext(),AvailableWorkers.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(GeoLocation.this,resultData.getString(Constants.RESULT_DATA_KEY),Toast.LENGTH_SHORT).show();
            }

        }
    }

}

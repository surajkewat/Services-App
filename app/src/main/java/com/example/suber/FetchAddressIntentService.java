package com.example.suber;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FetchAddressIntentService extends IntentService {

    private ResultReceiver resultReceiver;
    Location lastlocation;
    String SAddress;
    String Scity;

    public FetchAddressIntentService(){
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent != null){
            String errorMessage="";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location= intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            if(location  == null){
                return;
            }
            Geocoder geocoder =new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            }
            catch (Exception e){
                errorMessage = e.getMessage();
            }
            if(addresses == null || addresses.isEmpty()){
                deliverResultToReceiver(Constants.FAILURE_RESULT,errorMessage,"NA","NA");
            }
            else {
                String addressStr = addresses.get(0).getAddressLine(0);
                //String city = addresses.get(0).getAddressLine();
                String city1 = addresses.get(0).getSubAdminArea();
                //String country = addresses.get(0).getAddressLine(2)
                String knownName1=addresses.get(0).getFeatureName();
                String Locality=addresses.get(0).getLocality();
                if(city1!=null)
                {
                    Scity=city1;
                }
                else if(knownName1!=null)
                {
                    Scity=knownName1;
                }
                else if(Locality!=null)
                {
                    Scity=Locality;
                }
                Address address =addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();
                for(int i=0;i<=address.getMaxAddressLineIndex();i++){
                    addressFragments.add(address.getAddressLine(i));
                }
                deliverResultToReceiver(
                        Constants.SUCCESS_RESULT,
                        TextUtils.join(
                                Objects.requireNonNull(System.getProperty("line.separator")),
                                addressFragments
                        ),
                        Locality,
                        addressStr
                );
            }
        }
    }

    private void deliverResultToReceiver(int resultCode,String addressMessage,String city ,String addressStr ){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY,addressMessage);
        bundle.putString("City",city);
        bundle.putString("Address",addressStr);
        resultReceiver.send(resultCode,bundle);
    }
}

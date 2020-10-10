package com.example.suber;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.FragmentActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.location.Address;
        import android.location.Geocoder;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.MapFragment;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.libraries.places.api.Places;
        import com.google.android.libraries.places.api.model.Place;
        import com.google.android.libraries.places.api.net.PlacesClient;
        import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
        import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

        import java.io.IOException;
        import java.util.Arrays;
        import java.util.List;
        import java.util.Locale;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    Button btnConfirmLoc;
    private GoogleMap mMap;
    private Geocoder mGeocoder ;
    private LatLng sydney = new LatLng(-8.579892, 116.095239);
    private MapFragment mapFragment;
    PlacesClient placesClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btnConfirmLoc = findViewById(R.id.btnConfirmLoc);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        String apiKey = "Your API Key";

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(MapActivity.this, apiKey);
        }
        // Retrieve a PlacesClient (previously initialized - see MainActivity)
        placesClient = Places.createClient(this);

        btnConfirmLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this,AvailableWorkers.class);
                startActivity(intent);
            }
        });


        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        final LatLng latLng = place.getLatLng();
                        sydney = latLng;
                        mapFragment.getMapAsync(MapActivity.this);
                        try {
                            getPlaceInfo(latLng.latitude,latLng.longitude);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(MapActivity.this, ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.5f));
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
        }
    }
    private void getPlaceInfo(double lat, double lon) throws IOException {
        mGeocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);

        if (addresses.get(0).getLocality() != null) {
            String city = addresses.get(0).getLocality();
            SharedPreferences sharedPreferences = getSharedPreferences("address", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("City",city);
            editor.commit();

            Log.i("MapActivity : ",sharedPreferences.getString("City",null));
            Toast.makeText(this,"City = "+ city,Toast.LENGTH_SHORT).show();
        }
    }

}

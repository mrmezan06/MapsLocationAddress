package com.mezan.mapdemo;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitude=0.0;
    double longitude=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        latitude = Double.parseDouble(bundle.getString("LAT"));
        longitude = Double.parseDouble(bundle.getString("LON"));

        Log.d("Maps-L", String.valueOf(latitude));
        Log.d("Maps-L", String.valueOf(longitude));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng cAddress = new LatLng(latitude, longitude);
        //mMap.addMarker(new MarkerOptions().position(cAddress).title("Marker in Current Location"));

        String LocalAddress=setAddress(latitude,longitude);

        mMap.addMarker(new MarkerOptions().position(cAddress).title(LocalAddress));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cAddress,12f));


    }

    private String setAddress(Double latitude, Double longitude){

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses.size() > 0) {
           // Log.d("Address Extra", "Extra " + addresses.get(0).getMaxAddressLineIndex());

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            /*String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            Log.d("Address","address:"+address);
            Log.d("Address","city:"+city);
            Log.d("Address","state:"+state);
            Log.d("Address","country:"+country);
            Log.d("Address","postal:"+postalCode);
            Log.d("Address","knownName:"+knownName);
*/
            Log.d("Address","address:"+address);

            addresses.get(0).getAdminArea();
           /* textView.setText(address);
            tvFeature.setText(knownName);
            tvCountry.setText(country);
            tvState.setText(state);
            tvCity.setText(city);
            tvPincode.setText(postalCode);*/
           return address;
        }

        return "No Local Address Found!";

    }
}

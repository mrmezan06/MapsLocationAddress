package com.mezan.mapdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class FetchLocation extends AppCompatActivity {

    public  static final int RequestPermissionCode  = 1 ;
    Button buttonEnable, buttonGet,btnMaps ;
    TextView textViewLongitude, textViewLatitude ;
    Context context;
    Intent intent1 ;
    LocationManager locationManager ;
    boolean GpsStatus = false ;
    Criteria criteria ;
    String Holder;
    FusedLocationProviderClient fusedLocationClient;
    String Lat = "";
    String Lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_location);

        EnableRuntimePermission();

        buttonEnable = findViewById(R.id.button);
        buttonGet = findViewById(R.id.button2);
        btnMaps = findViewById(R.id.maps);

        textViewLongitude = findViewById(R.id.textView);
        textViewLatitude = findViewById(R.id.textView2);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);

        context = getApplicationContext();

        CheckGpsStatus();

        buttonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);


            }
        });

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckGpsStatus();

                if(GpsStatus == true) {
                    if (Holder != null) {
                        if (ActivityCompat.checkSelfPermission(
                                FetchLocation.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                &&
                                ActivityCompat.checkSelfPermission(FetchLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        /*This line is main work in location and must include gradle of google play services and also coarse location and internet connection must be enable*/

                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(FetchLocation.this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if(location != null){
                                            Log.d("Latitude", String.valueOf(location.getLongitude()));
                                            Log.d("Longitude",String.valueOf(location.getLatitude()));

                                          Lat = String.valueOf(location.getLatitude());
                                          Lon = String.valueOf(location.getLongitude());

                                            textViewLongitude.setText("Latitude:"+location.getLatitude());
                                            textViewLatitude.setText("Longitude:"+location.getLongitude());
                                            btnMaps.setEnabled(true);
                                        }else {
                                            textViewLongitude.setText("Latitude is not found");
                                            textViewLatitude.setText("Latitude is not found");
                                            btnMaps.setEnabled(false);
                                        }
                                    }
                                });
                        /*Location(Latitude,Longitude)*/



                    }
                }else {

                    Toast.makeText(FetchLocation.this, "Please Enable GPS First", Toast.LENGTH_LONG).show();
                    btnMaps.setEnabled(false);

                }
            }
        });

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Maps Activity Intent Start
                Intent intent=new Intent(FetchLocation.this,MapsActivity.class);
                intent.putExtra("LAT",Lat);
                intent.putExtra("LON",Lon);
                startActivity(intent);

            }
        });
    }

    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(FetchLocation.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {

            Toast.makeText(FetchLocation.this,"ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(FetchLocation.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(FetchLocation.this,"Permission Granted, Now your application can access GPS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(FetchLocation.this,"Permission Canceled, Now your application cannot access GPS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}

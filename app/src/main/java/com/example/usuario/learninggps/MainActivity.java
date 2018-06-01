package com.example.usuario.learninggps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback{

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    LocationManager locationManager = null;
    LocationProvider provider = null;
    TextView text1;

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text1 = (TextView) findViewById(R.id.text1);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);

        String providerName = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(providerName,1000,0,this);
        Location mylocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        text1.setText(String.valueOf(mylocation.getLongitude()));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                    provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setCostAllowed(false);

                    String providerName = locationManager.getBestProvider(criteria, true);


                    // If no suitable provider is found, null is returned.
                    if (providerName != null) {

                    }


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // This verification should be done during onStart() because the system calls
        // this method when the user returns to the activity, which ensures the desired
        // location provider is enabled each time the activity resumes from the stopped state.
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            enableLocationSettings();
        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
        Location mylocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d("CHANGED", "LOCATION UPDATED" + String.valueOf(mylocation.getLongitude()));
        text1.setText(String.valueOf(mylocation.getLongitude()));
        onMapReady(mMap,mylocation);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-3.7462154, -38.5724332);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Furtaleza, ICA"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    public void onMapReady(GoogleMap googleMap, Location mylocation) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Furtaleza, ICA"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


}

package com.example.crowdsteering;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BackgroundService extends Service implements com.google.android.gms.location.LocationListener {
    private GoogleMap mMap;


    LocationListener locationListener;
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Marker mCurrLocationMarker;

    String provider;
    Location lastlocate;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    TextView locate, target;
    LatLng India = new LatLng(20.5937, 78.9629);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getlocation();



        return START_STICKY;
    }

    private void getlocation() {
        //last location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);


        lastlocate = locationManager.getLastKnownLocation(provider);
        if (lastlocate != null) {
            double lat = 20.5937;
            lat = lastlocate.getLatitude();
            double lng = 78.9629;
            lng = lastlocate.getLongitude();


            Toast.makeText(getApplicationContext(), "" + lat + lng + "", Toast.LENGTH_SHORT).show();
            //mMap.Marker(new MarkerOptions().position(new LatLng(lat, lng)));
            LatLng latLng = new LatLng(lastlocate.getLatitude(), lastlocate.getLongitude());


            //move map camera

        } else {

            Toast.makeText(getApplicationContext(), "current location not available", Toast.LENGTH_SHORT).show();
//here

        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location", location.toString());

                double lat = location.getLatitude();
                double lng = location.getLongitude();
                // mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));

                Toast.makeText(getApplicationContext(), getAddres(getApplicationContext(), lat, lng), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        String bestProvider = locationManager.getBestProvider(criteria, true);

//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(getApplicationContext(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
//        }
//        else {
        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);

//        }
//
    }

    public String getAddres(Context ctx, double lat, double lng) {
        String fulladdress = null;
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                fulladdress = address.getAddressLine(0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return fulladdress;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        Toast.makeText(this,""+ latitude+longitude+" ", Toast.LENGTH_SHORT).show();
    }
}

package com.example.myapplication.ui;

import static androidx.core.content.ContextCompat.getSystemService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.MapsActivity;
import com.example.myapplication.ui.cars.HomeFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.provider.Settings;

import com.example.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;


public class MapsFragment extends Fragment {
    //private GPSTracker gpsTracker;
    private View rootView;
    HashMap<String, String> markerMap = new HashMap<String, String>();
    private GoogleMap mMap;
    LocationManager locationManager;
    public double tvLatitude, tvLongitude;
    public static double latitude=0, longitude=0;
    public LatLng userLocation;
    boolean a=true;

    FusedLocationProviderClient client;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);


        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapsFragment);





        // Async map

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {


                LatLng santos = new LatLng(38.70843814152426, -9.15501526730533);

                getCurrentLocation();

                LatLng userLocation = new LatLng(latitude, longitude);
                Log.e("User locationnnnnnnnn",""+userLocation);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
                LatLng userLive = new LatLng(tvLatitude, tvLongitude);

                //When map is loaded

                Marker markerOne = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(userLocation));

                if(a){
                    LatLng userLocation1 = new LatLng(latitude, longitude);
                   // Marker userMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(userLocation));
                    googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(userLocation1));
                    a=false;
                }

                               String firstid = markerOne.getId();


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions = new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        String firstid = markerOne.getId();
                        markerMap.put(firstid, "action_first");
                        //Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        //Add marker on map
                        googleMap.addMarker(markerOptions);

                    }
                });
            }
        });
        //Initialize Location client
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        //Check condition
        if (ContextCompat.checkSelfPermission(getActivity()
                , Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            //When permission is granted
            //Call method
            getCurrentLocation();
        }else{
            //When permissiojn is not granted
            //REquest permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        return view;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if(requestCode == 100 && (grantResults.length > 0) &&(grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
            //When permission is granted
            //Call method
            getCurrentLocation();
        }else{
            //When permission is denied
            //Display toast
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //Initialize location manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //When location service is enabled
            //Get last location
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location
                    Location location = task.getResult();
                    //Check condition
                    if(location != null){
                        //When Location result is not null
                        //Set latitude
                        tvLatitude = location.getLatitude();
                        Log.e("latitude before if",""+latitude);
                        if (latitude==0)
                        latitude = tvLatitude;
                        Log.e("latitude after if",""+latitude);
                        Log.e("Latitude",""+tvLatitude);
                        //Set longitude
                        tvLongitude = location.getLongitude();
                        Log.e("longitude before if",""+longitude);
                        if (longitude==0)
                        longitude = tvLongitude;
                        Log.e("longitude after if",""+longitude);
                        Log.e("Longitude",""+tvLongitude);

                    }else{
                        //When location result is null
                        //Initialize location request
                        com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        //Initialize location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                //Initialize location
                                Location location1 = locationResult.getLastLocation();
                                //set latitude
                                tvLatitude = location.getLatitude();
                                //Set longitude
                                tvLongitude = location.getLongitude();
                            }
                        };
                        //Request location updates
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    }
                }
            });
        }else{
            //When location service is not enabled
            //Open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }
}
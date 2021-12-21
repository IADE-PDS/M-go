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
import com.example.myapplication.PostPersons;
import com.example.myapplication.data.LoginDataSource;
import com.example.myapplication.databinding.FragmentMapsBinding;
import com.example.myapplication.databinding.FragmentWhenBinding;
import com.example.myapplication.ui.addCars.DashboardViewModel;
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
import androidx.navigation.Navigation;

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
import android.widget.Button;
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
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsFragment extends Fragment {
    //private GPSTracker gpsTracker;
    private View rootView;
    HashMap<String, String> markerMap = new HashMap<String, String>();
    private GoogleMap mMap;
    LocationManager locationManager;
    public double tvLatitude, tvLongitude;
    public static double latitude = 0, longitude = 0;
    public LatLng userLocation;
    boolean a = true;
    private FloatingActionButton red,blue;
    private FragmentMapsBinding binding;
    FusedLocationProviderClient client;
    private LatLng markerCoordinates;
    boolean e=false;
    public static LatLng place;
    GoogleMap mapss;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        red=binding.floatingActionButton;
        blue=binding.floatingActionButton2;


        // Initialize view
        //View view = inflater.inflate(R.layout.fragment_maps, container, false);


        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapsFragment);


        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mapss=googleMap;

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                getCurrentLocation();

                //Log.e("User locationnnnnnnnn",""+googleMap.getMyLocation());
                LatLng userLocation = new LatLng(latitude,longitude);
                Log.e("User locationnnnnnnnn",""+userLocation);

                LatLng userLive = new LatLng(latitude, longitude);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLive, 16));



                //When map is loaded

               // Marker markerOne = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(userLocation));



                if(a){
                    LatLng userLocation1 = new LatLng(latitude, longitude);
                   // Marker userMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(userLocation));
                    googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(userLocation1));
                    a=false;
                }

                            //   String firstid = markerOne.getId();


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
                       // String firstid = markerOne.getId();
                        //markerMap.put(firstid, "action_first");
                        //Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        //Add marker on map
                        googleMap.addMarker(markerOptions);
                        LatLng userLocation = new LatLng(latitude, longitude);
                        Marker markertwo = googleMap.addMarker(new MarkerOptions().position(userLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        markerCoordinates=latLng;
                    }
                });


            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("test",""+markerCoordinates+"");



                Map<String, String> postData = new HashMap<>();
                postData.put("repairLat", "" + markerCoordinates.latitude );
                postData.put("repairLong", "" + markerCoordinates.longitude );

                PostPersons taks1 = new PostPersons(postData);
                Log.e("taks1 data", ""+postData.toString());
                taks1.execute("https://mechanic-on-the-go.herokuapp.com/api/repairs/post/"+ HomeFragment.idRepair);

                Navigation.findNavController(view)
                        .navigate(R.id.action_maps_fragment_to_navigation_notifications);
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng myLocation = new LatLng(latitude,longitude);
                Log.e("test",""+myLocation+"");

                Map<String, String> postData = new HashMap<>();
                postData.put("repairLat", "" + myLocation.latitude );
                postData.put("repairLong", "" + myLocation.longitude );

                PostPersons taks1 = new PostPersons(postData);
                Log.e("taks1 data", ""+postData.toString());
                taks1.execute("https://mechanic-on-the-go.herokuapp.com/api/repairs/post/"+ HomeFragment.idRepair);

                Toast.makeText(getContext(), "Pedido enviado", Toast.LENGTH_LONG).show();
                Navigation.findNavController(view)
                        .navigate(R.id.action_maps_fragment_to_navigation_notifications);
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

        return root;


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
                        e=true;


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
       // if (e)
          //  mapss.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16));
        e=true;



        private void getDeviceLocation() {
            /*
             * Get the best and most recent location of the device, which may be null in rare
             * cases when a location is not available.
             */
            try {
                if (locationPermissionGranted) {
                    Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                // Set the map's camera position to the current location of the device.
                                lastKnownLocation = task.getResult();
                                if (lastKnownLocation != null) {
                                    mapss.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(lastKnownLocation.getLatitude(),
                                                    lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                }
                            } else {
                                Log.d("", "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                                mapss.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                                mapss.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
                }
            } catch (SecurityException e)  {
                Log.e("Exception: %s", e.getMessage(), e);
            }
        }




    }
}
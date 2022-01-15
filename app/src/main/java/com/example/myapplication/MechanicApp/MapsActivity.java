package com.example.myapplication.MechanicApp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG = "directions";
    //Define list to get all latlng for the route
    private List<LatLng> calculatedPath = null;
    Double latitude;
    Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("Latitude",0);
        longitude = intent.getDoubleExtra("Longitude",0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng IADE = new LatLng(38.7073002821223, -9.1524541555281883);
        mMap.addMarker(new MarkerOptions().position(IADE).title("Marker in Barcelona"));

        LatLng repair = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(repair).title("Marker in Madrid"));

        new GetPathFromLocation(IADE, repair, new DirectionPointListener() {
            @Override
            public void onPath(PolylineOptions polyLine) {
                mMap.addPolyline(polyLine);
            }
        }).execute();

        LatLng zaragoza = new LatLng(latitude,longitude);

        calculatedPath = GetDirections(IADE, repair);
        if(calculatedPath!=null)
        {
            //Draw the polyline
            if (calculatedPath.size() > 0) {
                PolylineOptions opts = new PolylineOptions().addAll(calculatedPath).color(Color.BLUE).width(5);
                mMap.addPolyline(opts);
            }
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza, 15));
    }

    private List<LatLng> GetDirections(LatLng a, LatLng b) {

        List<LatLng> directionsPath = new ArrayList();

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyDxWOoqBAMz5Bsu0j22ZXrd0uEA_abIHxo")
                .build();
        // Set the direction request
        DirectionsApiRequest req = DirectionsApi.getDirections(
                context,
                a.latitude+","+a.longitude,
                b.latitude+","+b.longitude
        );

        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0)
            {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null)
                {
                    for(int i=0; i<route.legs.length; i++)
                    {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null)
                        {
                            for (int j=0; j<leg.steps.length;j++)
                            {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0)
                                {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null)
                                        {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1)
                                            {
                                                directionsPath.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null)
                                    {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            directionsPath.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return directionsPath;
        } catch(Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
            return null;
        }
    }
}

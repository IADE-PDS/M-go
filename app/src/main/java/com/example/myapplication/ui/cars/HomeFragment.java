package com.example.myapplication.ui.cars;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.Downloaders.JSONArrayDownloader;
//import com.example.myapplication.JSONArrayDownloader;
import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.LoginDataSource;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.ui.addCars.DashboardViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {
    Button btnGps,btnChoose;
    ListView repairs;
    Spinner car;
    LoginDataSource id = new LoginDataSource();



    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList<String> typeRepairId;
    ArrayList<String> typeRepairNames;
    ArrayList<String> modelsName;
    ArrayList<String> brandNames;
    ArrayList<String> clientcars;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    public double latitude,longitude;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        repairs= binding.listRepairs;
        btnChoose=binding.btnChoose;
        btnGps = binding.btnGps;
        car = binding.carSelector;

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_navigation_home_to_When);
            }
        });

        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity maps=new MapsActivity();
                getLocation();
                Intent intent=new Intent(getContext(), MapsActivity.class);

                intent.putExtra("Latitude", Double.toString(latitude));
                intent.putExtra("Longitude", Double.toString(longitude));
                startActivity(intent);
            }
        });




        JSONArrayDownloader task = new JSONArrayDownloader();
        JSONArray objTypeRepair;
        JSONArray objCar = null;

        //Gets the cars of a client
        JSONObject clientcar;

        Log.e("",""+LoginDataSource.idint);

        try {
            objCar = task.execute("https://mechanic-on-the-go.herokuapp.com/api/cars/client/"+ DashboardViewModel.getClientId(LoginDataSource.idint)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objCar = null;
        }





        modelsName = new ArrayList<>();
        brandNames = new ArrayList<>();
        if(objCar != null) {
            for(int i = 0; i < objCar.length(); i++) {
                try {

                    clientcar = objCar.getJSONObject(i).getJSONObject("carModel");
                    modelsName.add(clientcar.getString("modelName"));
                    brandNames.add(clientcar.getJSONObject("modelBrand").getString("brandName"));
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }


        clientcars = new ArrayList<>();

        if (brandNames.size()>1){
            for (int i=0;i<modelsName.size();i++){
                clientcars.add(brandNames.get(i)+" "+modelsName.get(i));

            }
        }
        else {
            clientcars.add(brandNames.get(0)+" "+modelsName.get(0));
        }



        JSONArrayDownloader task1 = new JSONArrayDownloader();
        JSONObject obj;

        ArrayAdapter<String> clientcaradapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, clientcars);
        clientcaradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        car.setAdapter(clientcaradapter);
        try {
            objTypeRepair = task1.execute("https://mechanic-on-the-go.herokuapp.com/api/typeRepair").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objTypeRepair = null;
        }

        typeRepairId = new ArrayList<>();
        typeRepairNames = new ArrayList<>();
        if(objTypeRepair != null) {
            for(int i = 0; i < objTypeRepair.length(); i++) {
                try {
                    obj = objTypeRepair.getJSONObject(i);
                    typeRepairId.add(obj.getString("id"));
                    typeRepairNames.add(obj.getString("typeRepairName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        initializeMyLListView();





        return root;
    }

    private void initializeMyLListView(){
        ArrayAdapter<String> myListAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_multiple_choice,typeRepairNames);
        repairs.setAdapter(myListAdapter);
        repairs.setItemChecked(3,true);
    }


    public void getLocation() {
        locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                MapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                MapsActivity.mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                MapsActivity.marker.setPosition(latLng);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) { }

            @Override
            public void onProviderEnabled(String s) { }

            @Override
            public void onProviderDisabled(String s) { }
        };

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                updateLocationInfo(location);
            }
        }
    }

    public void updateLocationInfo(Location location) {



        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        try {

            String address = "Could not find address";
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0 )
            {
                Log.i("PlaceInfo", listAddresses.get(0).toString());

                address = "Address: \n";

                if (listAddresses.get(0).getSubThoroughfare() != null) {
                    address += listAddresses.get(0).getSubThoroughfare() + " ";
                }

                if (listAddresses.get(0).getThoroughfare() != null) {
                    address += listAddresses.get(0).getThoroughfare() + "\n";
                }

                if (listAddresses.get(0).getLocality() != null) {
                    address += listAddresses.get(0).getLocality() + "\n";
                }

                if (listAddresses.get(0).getPostalCode() != null) {
                    address += listAddresses.get(0).getPostalCode() + "\n";
                }

                if (listAddresses.get(0).getCountryName() != null) {
                    address += listAddresses.get(0).getCountryName() + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening()
    {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == btnChoose.getId()){
            String itemSelected = "Select items: \n";
            for(int i=0;i<repairs.getCount();i++){
                if(repairs.isItemChecked(i)){
                    itemSelected += repairs.getItemAtPosition(i) +"\n";
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
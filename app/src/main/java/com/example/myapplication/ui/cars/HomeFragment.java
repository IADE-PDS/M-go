package com.example.myapplication.ui.cars;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.Downloaders.JSONArrayDownloader;
//import com.example.myapplication.JSONArrayDownloader;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MapsActivity;
import com.example.myapplication.PostPersons;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {




    Button btnGps,btnChoose;
    ListView repairs;
    Spinner car;
    LoginDataSource id = new LoginDataSource();


    public static String idRepair;


    public JSONArray post;
    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList<String> typeRepairId;
    ArrayList<String> typeRepairNames;
    ArrayList<String> modelsName;
    ArrayList<String> brandNames;
    ArrayList<String> brandid;
    ArrayList<String> clientcars;
    ArrayList<String> carId;


    private FragmentHomeBinding binding;
    public double latitude,longitude;
    public static LatLng latLng;


    public class Item {
        boolean checked;
        Drawable ItemDrawable;
        String ItemString;
        Item( String t, boolean b){
            ItemString = t;
            checked = b;
        }

        public boolean isChecked(){
            return checked;
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        ImageView icon;
        TextView text;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // LAyout da tua checkbocks
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.row, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            /*
            * Quando esta selecionado ou nao
            * */

            viewHolder.icon.setImageDrawable(list.get(position).ItemDrawable);
            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);


            /**
             *
             * Mete cruz ou trira cruz
             *
             * */
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;
                    Toast.makeText(getContext(),
                            itemStr + "setOnClickListener\nchecked: " + newState,
                            Toast.LENGTH_LONG).show();
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
    }

    Button btnLookup;
    List<Item> items;
    ListView listView;
    ItemsListAdapter myItemsListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        repairs= binding.listRepairs;
        btnChoose=binding.btnChoose;
        btnGps = binding.btnGps;
        car = binding.carSelector;
        listView = binding.listRepairs;
        btnLookup = binding.btnChoose;





        listView.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(getContext(),
                ((Item)(parent.getItemAtPosition(position))).ItemString,
                Toast.LENGTH_LONG).show());
        
        /**
         * 
         * Ve quais estao selecionados
         * */
        
        btnLookup.setOnClickListener(view -> {
            String str = "Check items:\n";

            for (int i=0; i<items.size(); i++){
                if (items.get(i).isChecked()){
                    str += i + "\n";
                }
            }


            Toast.makeText(getContext(),
                    str,
                    Toast.LENGTH_LONG).show();

        });


        btnChoose.setOnClickListener(view -> {
            ArrayList<String> idselected;
            idselected = new ArrayList<>();
            boolean execption=false;
            int sum=0;


            for (int i = 0; i < items.size(); i++) {

                if (items.get(i).checked) {
                    idselected.add(typeRepairId.get(i));
                    execption=true;
                    sum++;
                }

            }
            if(sum!=0) {
                if (execption = false) {

                } else {
                    Log.e("super importante id do carro a passar no post", "" + carId.get(car.getSelectedItemPosition()));

                    Map<String, String> postData = new HashMap<>();
                    postData.put("repairCar", carId.get(car.getSelectedItemPosition()));


                    PostPersons taks1 = new PostPersons(postData);
                    Log.e("PostData;   ", "" + postData);
                    try {
                        post = taks1.execute("https://mechanic-on-the-go.herokuapp.com/api/repairs").get();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {

                        JSONObject obj1;

                        if (post != null) {
                            for (int u = 0; u < post.length(); u++) {
                                try {
                                    obj1 = post.getJSONObject(u);
                                    idRepair = (obj1.getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        for (int i = 0; i < idselected.size(); i++) {
                            Log.e("post dos Type repairs a funcionar", "maybe");


                            /**
                             * Vai sempre a primeria
                             * */

                            Map<String, String> postData1 = new HashMap<>();
                            postData1.put("typeRepairRepairTypeRepairId", typeRepairId.get(i));
                            postData1.put("typeRepairRepairRepairId", idRepair);


                            PostPersons taks2 = new PostPersons(postData1);
                            taks2.execute("https://mechanic-on-the-go.herokuapp.com/api/typeRepairRepair");

                        }


                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                }
                Navigation.findNavController(view)
                        .navigate(R.id.action_navigation_home_to_When);
            }else{
                Toast.makeText(getContext(), "choose repair", Toast.LENGTH_SHORT).show();
            }

        });

        btnGps.setVisibility(View.INVISIBLE);
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




        Log.e("","login idddddddddddddddddddddddddddddddddddddddddd"+LoginDataSource.idint);



        try {
            objCar = task.execute("https://mechanic-on-the-go.herokuapp.com/api/cars/client/"+ DashboardViewModel.getClientId(LoginDataSource.idint)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objCar = null;
        }

        carId= new ArrayList<>();
        modelsName = new ArrayList<>();
        brandNames = new ArrayList<>();
        brandid = new ArrayList<>();
        if(objCar != null) {
            for(int i = 0; i < objCar.length(); i++) {
                try {
                    // Arzaenar id de todos os carros do cliente
                    carId.add(objCar.getJSONObject(i).getString("id"));

                    clientcar = objCar.getJSONObject(i).getJSONObject("carModel");
                    Log.e("carId", ""+clientcar.getString("id"));
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



        JSONArrayDownloader task3 = new JSONArrayDownloader();
        JSONObject obj;

        ArrayAdapter<String> clientcaradapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, clientcars);
        clientcaradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        car.setAdapter(clientcaradapter);
        try {
            objTypeRepair = task3.execute("https://mechanic-on-the-go.herokuapp.com/api/typeRepair").get();
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




        //initializeMyLListView();

        initItems();
        myItemsListAdapter = new ItemsListAdapter(getContext(), items);
        listView.setAdapter(myItemsListAdapter);



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


    /*
    *Explica este o zee
    *
    * */

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

    private void initItems(){
        items = new ArrayList<Item>();

        //TypedArray arrayDrawable = getResources().obtainTypedArray(R.array.resicon);
        TypedArray arrayText = getResources().obtainTypedArray(R.array.marca);

        for(int i=0; i<typeRepairNames.size(); i++){
            Log.e("type repair inside init",""+typeRepairNames);
            String s = ""+typeRepairNames.get(i);
           // String s = arrayText.getString(i);
            boolean b = false;
            Item item = new Item(s, b);
            items.add(item);
        }


        arrayText.recycle();
    }


}

//implement http://android-er.blogspot.com/2017/03/android-listview-with-checkbox.html
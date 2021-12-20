package com.example.myapplication.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.Downloaders.JSONArrayDownloader;
import com.example.myapplication.R;
import com.example.myapplication.data.LoginDataSource;
import com.example.myapplication.databinding.FragmentMechanicBinding;
import com.example.myapplication.databinding.FragmentOnGoingRepairsBinding;
import com.example.myapplication.ui.addCars.DashboardViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class OnGoingRepairs extends Fragment {
    FragmentOnGoingRepairsBinding binding;
    ListView listView;
    ArrayList<String> repairs,carId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOnGoingRepairsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView= binding.list;
        repairs = new ArrayList<>();

        JSONArrayDownloader task3 = new JSONArrayDownloader();
        JSONObject obj;
        ArrayAdapter<String> clientcaradapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, repairs);
        clientcaradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listView.setAdapter(clientcaradapter);



        JSONArrayDownloader task = new JSONArrayDownloader();
        JSONArray objTypeRepair;
        JSONArray objCar = null;

        //Gets the cars of a client
        JSONObject clientcar;

        Log.e("",""+ LoginDataSource.idint);

        try {
            objCar = task.execute("https://mechanic-on-the-go.herokuapp.com/api/cars/client/"+ DashboardViewModel.getClientId(LoginDataSource.idint)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objCar = null;
        }

        carId= new ArrayList<>();

        if(objCar != null) {
            for(int i = 0; i < objCar.length(); i++) {
                try {
                    carId.add(objCar.getJSONObject(i).getString("id"));
                    clientcar = objCar.getJSONObject(i).getJSONObject("carModel");

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }

        JSONArrayDownloader task1 = new JSONArrayDownloader();
        JSONArray objCar1 = null;

        //Gets the cars of a client
        JSONObject clientcar1;


        for(int i=0; i<carId.size();i++){


            Log.e("",""+ LoginDataSource.idint);

            try {
                objCar1 = task1.execute("https://mechanic-on-the-go.herokuapp.com/api/repairs/car/"+carId.get(i)).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                objCar1 = null;
            }


            if(objCar1 != null) {
                for(int o = 0; 0 < objCar1.length(); o++) {
                    try {

                       // clientcar.getJSONObject("modelBrand").getString("brandName")
                        carId.add(objCar1.getJSONObject(o).getString("id"));
                        clientcar = objCar1.getJSONObject(o).getJSONObject("repairTaken");

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }
        }

        // Inflate the layout for this fragment
        return root;
    }
}
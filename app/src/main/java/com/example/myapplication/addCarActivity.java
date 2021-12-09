package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.example.myapplication.Downloaders.JSONArrayDownloader;
import com.example.myapplication.data.LoginDataSource;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.ui.addCars.DashboardViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class addCarActivity extends AppCompatActivity {

    private FragmentDashboardBinding binding;
    private RequestQueue queue;
    private String[] transmissao = {"Automatica", "Manual"};
    private String[] fuel = {"Gasoleo", "Gasolina"};
    Spinner spinnerBrands, spinnermodels, spinnerengine, spinnertransmission,spinneryear,spinnerfuel;
    Button add;
    EditText numberplate;
    JSONArray persons = null;
    ArrayList<String> brandsId;
    ArrayList<String> brandNames;
    ArrayList<String> modelsId;
    ArrayList<String> modelsName;
    ArrayList<String> engineId;
    ArrayList<String> engineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);

        ArrayList<String> yearList = new ArrayList<>();

        for (int i = Calendar.getInstance().get(Calendar.YEAR); i >=1920 ; i--){
            yearList.add(Integer.toString(i));
        }

        spinnerengine = findViewById(R.id.motor);
        spinnerfuel = findViewById(R.id.fuel);
        spinnerBrands = findViewById(R.id.marca);
        spinnertransmission = findViewById(R.id.transmissao);
        spinnermodels = findViewById(R.id.modelo);
        numberplate = findViewById(R.id.NumberPlate);
        spinneryear = findViewById(R.id.year);
        add = findViewById(R.id.adicionar);



        JSONArrayDownloader task = new JSONArrayDownloader();
        JSONArray objBrands;
        try {
            objBrands = task.execute("https://mechanic-on-the-go.herokuapp.com/api/brands").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objBrands = null;
        }

        JSONObject obj;
        brandsId = new ArrayList<>();
        brandNames = new ArrayList<>();
        if(objBrands != null) {
            for(int i = 0; i < objBrands.length(); i++) {
                try {
                    obj = objBrands.getJSONObject(i);
                    brandsId.add(obj.getString("id"));
                    brandNames.add(obj.getString("brandName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        ArrayAdapter<String> brandadapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brandNames);
        brandadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrands.setAdapter(brandadapter);

        spinnerBrands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                JSONArray spotsArray = null;
                Log.e("thisissss",""+ brandsId.get(0));
                JSONArrayDownloader task = new JSONArrayDownloader();
                String url = "https://mechanic-on-the-go.herokuapp.com/api/models/brand/" + iddeveloper(brandNames, brandsId,spinnerBrands);
                try {
                    spotsArray = task.execute(url).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    spotsArray = null;
                }

                JSONObject obj;
                modelsName = new ArrayList<>();
                modelsId = new ArrayList<>();

                if (spotsArray != null) {
                    for (int i = 0; i < spotsArray.length(); i++) {
                        try {
                            obj = spotsArray.getJSONObject(i);
                            modelsName.add(obj.getString("modelName"));
                            modelsId.add(obj.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                ArrayAdapter<String> modeladapter = new ArrayAdapter<String>(addCarActivity.this, android.R.layout.simple_spinner_item, modelsName);
                modeladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnermodels.setAdapter(modeladapter);



                JSONArray engineArray = null;
                JSONArrayDownloader task1 = new JSONArrayDownloader();
                Log.e("", "");
                String url1 = "https://mechanic-on-the-go.herokuapp.com/api/modelengines/"+ iddeveloper(modelsName, modelsId, spinnermodels);
                try {
                    engineArray = task1.execute(url1).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    engineArray = null;
                }

                JSONObject obj1;
                engineName = new ArrayList<>();
                engineId = new ArrayList<>();

                if (engineArray != null) {
                    for (int i = 0; i < engineArray.length(); i++) {
                        try {
                            obj1 = engineArray.getJSONObject(i);
                            engineName.add(obj1.getString("engineName"));
                            engineId.add(obj1.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.e("models",""+modelsName.toString()) ;


                ArrayAdapter<String> engineAdapter = new ArrayAdapter<String>(addCarActivity.this, android.R.layout.simple_spinner_item, engineName);
                engineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerengine.setAdapter(engineAdapter);

                //Log.e("this",enginelist.toString());
            }




            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        test();





        ArrayAdapter<String> transAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, transmissao);
        transAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertransmission.setAdapter(transAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneryear.setAdapter(yearAdapter);



        ArrayAdapter<String> fuelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fuel);
        fuelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerfuel.setAdapter(fuelAdapter);




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    Map<String, String> postData = new HashMap<>();
                    postData.put("carLicensePlate", numberplate.getText().toString());
                    postData.put("carYear", spinneryear.getSelectedItem().toString());
                    postData.put("carClientId", ""+DashboardViewModel.getClientId(LoginDataSource.idint));
                    postData.put("carModelId", iddeveloper(modelsName,modelsId,spinnerBrands));
                    postData.put("carBrandId", iddeveloper(brandNames,brandsId,spinnerBrands));
                    postData.put("carTransmission", spinnertransmission.getSelectedItem().toString());
                    postData.put("carFuel", spinnerfuel.getSelectedItem().toString());
                    postData.put("carEngineId", iddeveloper(engineName,engineId,spinnerengine));


                    PostPersons taks1 = new PostPersons(postData);
                    taks1.execute("https://mechanic-on-the-go.herokuapp.com/api/cars");

                    Intent intent = new Intent(addCarActivity.this, MainActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                    persons = null;
                }
            }
        });


    }
    public void click(View v){
        spinnerBrands.getSelectedItem().toString();
        spinnermodels.getSelectedItem().toString();
        spinnerengine.getSelectedItem().toString();
        spinnertransmission.getSelectedItem().toString();


    }

    public String iddeveloper(ArrayList names, ArrayList ids,Spinner c){
        for (int temp = 0; temp < names.size(); temp++){
            if (c.getSelectedItem().equals(names.get(temp))){
                return ids.get(temp).toString();
            }

        }
        return null;
    }


    public void test(){


    }

}
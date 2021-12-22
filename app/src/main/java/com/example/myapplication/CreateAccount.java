package com.example.myapplication;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Downloaders.JSONArrayDownloader;
import com.example.myapplication.data.LoginDataSource;
import com.example.myapplication.ui.cars.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateAccount extends AppCompatActivity {
   public Button add;
   public EditText name,email,number,password1,password2,nif;
   public JSONArray NewAcc,client;
   public String id;
   String idClient;
    JSONArray person = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        add = findViewById(R.id.create);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        nif = findViewById(R.id.nif);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("this"," "+password1.getText().toString()+" "+password2.getText().toString());
                if (password1.getText().toString().equals(password2.getText().toString())){

                    try {


                        Map<String, String> postData = new HashMap<>();
                        postData.put("personName", name.getText().toString());
                        postData.put("personPhoneNumber", number.getText().toString());
                        postData.put("personEmail", email.getText().toString());
                        postData.put("personPassword", password1.getText().toString());



                        PostPersons taks1 = new PostPersons(postData);
                        try{
                           NewAcc = taks1.execute("https://mechanic-on-the-go.herokuapp.com/api/persons").get();

                        } catch (Exception e){
                            e.printStackTrace();
                            person = null;
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                        person = null;
                    }

                    try {

                        JSONObject obj1;

                        if (NewAcc != null) {
                            for (int i = 0; i < NewAcc.length(); i++) {
                                try {
                                    obj1 = NewAcc.getJSONObject(i);
                                    id=(obj1.getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        Map<String, String> postData = new HashMap<>();
                        postData.put("clientPerson", id);
                        Log.e("Client id on post",""+id);
                        postData.put("clientNif", nif.getText().toString());
                        PostPersons taks2 = new PostPersons(postData);

                        try{
                            client = taks2.execute("https://mechanic-on-the-go.herokuapp.com/api/clients").get();
                            Log.e("Clientttttttttttttttttttttttttttttttttttttttttttttt",""+client.toString());
                        } catch (Exception e){
                            e.printStackTrace();
                            person = null;
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                        person = null;
                    }

                    try {

                        JSONObject obj11;

                        if (client != null) {
                            for (int i = 0; i < client.length(); i++) {
                                try {
                                    obj11 = client.getJSONObject(i);
                                    Log.e("objetotototototoototot", ""+obj11);
                                    idClient = (obj11.getString("id"));
                                    Log.e("iddddddddddddclienttttttttttttttttttttt",""+idClient);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }






                    } catch (Exception e) {
                        e.printStackTrace();
                        person = null;
                    }


                    Intent intent = new Intent(CreateAccount.this, addCarActivity.class);
                    intent.putExtra("Idcliente", idClient );

                    startActivity(intent);


                }
                else{
                    Toast.makeText(CreateAccount.this, "The passwords don't coincide", Toast.LENGTH_SHORT).show();


                }


                LoginDataSource loginDataSource = new LoginDataSource();
                loginDataSource.login(email.getText().toString(), password1.getText().toString());
            }
        });


    }




}
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.ui.cars.HomeFragment;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {
   public Button add;
   public EditText name,email,number,password1,password2,nif;
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
                        taks1.execute("https://mechanic-on-the-go.herokuapp.com/api/persons");
                        Intent intent = new Intent(getApplicationContext(), addCarActivity.class);
                                               startActivity(intent);


                    } catch (Exception e) {
                        e.printStackTrace();
                        person = null;
                    }


                }
                else{
                    Toast.makeText(CreateAccount.this, "The passwords don't coincide", Toast.LENGTH_SHORT).show();
                }



                try {


                   // Map<String, String> postData = new HashMap<>();
                    //postData.put("clientPerson", name.getText().toString());
                    //postData.put("clientNif", nif.getText().toString());




                    //PostPersons taks1 = new PostPersons(postData);
                    //taks1.execute("https://mechanic-on-the-go.herokuapp.com/api/persons");



                } catch (Exception e) {
                    e.printStackTrace();
                    person = null;
                }
            }
        });



    }



}
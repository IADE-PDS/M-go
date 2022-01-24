package com.example.myapplication.data;

import android.util.Log;

import com.example.myapplication.Downloaders.IntegerDownloader;
import com.example.myapplication.Downloaders.JSONArrayDownloader;
import com.example.myapplication.data.model.LoggedInUser;

import com.example.myapplication.Downloaders.JSONObjDownloader;
import com.example.myapplication.ui.addCars.DashboardViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class LoginDataSource {

    private String id;
    public static String isMechanic;
    private JSONObject objLogin;
    public static int idint=1;


    //Esta funçao esta a ser usada na MainActivity
    public Result<LoggedInUser> login(String username, String password) {

        try {
            getJson(username, password);
            if (objLogin != null){
                LoggedInUser user =
                        new LoggedInUser(
                                id,
                                ""+username);
                return new Result.Success<>(user);
            } else {
                return new Result.Error(new IOException("Error logging in"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }


    public void getJson(String username, String password){
        JSONObjDownloader task = new JSONObjDownloader();
        // Este lin vai ser se existe um username com a pass indicada
        String url = "https://mechanic-on-the-go.herokuapp.com/api/persons/log/"+username+"/"+password;
        try {
            //Vais executar o url para ir buscar o json e armazenar no objLogin
            objLogin = task.execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objLogin = null;
        }

        Log.e("Login", ""+objLogin);

        try {
            // Esta a ir a chave numa ao json -> true ou false
            isMechanic = objLogin.getString("personIsMechanic");
            // Estamos na ir buscar o id da pessoa
            id = objLogin.getString("id");
            Log.e("id", id);


            //Guardar num public static void para poder aceder todo o lado
            // Nao saber como fazer o intente para fragmentos foi a unica forma
            idint=Integer.parseInt(id);
            Log.e("id", ""+idint);
            //

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void logout() {
        // TODO: revoke authentication
    }
}
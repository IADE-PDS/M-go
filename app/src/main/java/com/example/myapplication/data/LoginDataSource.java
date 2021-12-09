package com.example.myapplication.data;

import android.util.Log;
import com.example.myapplication.data.model.LoggedInUser;

import com.example.myapplication.Downloaders.JSONObjDownloader;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class LoginDataSource {

    private String id;
    public static String isMechanic;
    private JSONObject objLogin;
    public static int idint=1;

    public LoginDataSource() {

    }

    public int getId() {
        return idint;
    }


    public void setIdint(int idint) {
        this.idint = idint;
    }

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
        String url = "https://mechanic-on-the-go.herokuapp.com/api/persons/log/"+username+"/"+password;
        try {
            objLogin = task.execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objLogin = null;
        }

        Log.e("Login", ""+objLogin);

        try {
            isMechanic = objLogin.getString("personIsMechanic");
            id = objLogin.getString("id");
            Log.e("id", id);
            idint=Integer.parseInt(id);
            Log.e("id", ""+idint);
            setIdint(Integer.parseInt(id));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
package com.example.myapplication.ui.addCars;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Downloaders.IntegerDownloader;
import com.example.myapplication.Downloaders.JSONArrayDownloader;
import com.example.myapplication.data.LoginDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the add page");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public static Integer getClientId(int id) {
        IntegerDownloader task = new IntegerDownloader();
        Integer IntegerClientId;
        try {
            IntegerClientId = task.execute("https://mechanic-on-the-go.herokuapp.com/api/clients/person/" + id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            IntegerClientId = null;
        }


        return IntegerClientId;
    }
}
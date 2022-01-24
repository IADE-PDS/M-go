package com.example.myapplication.Downloaders;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IntegerDownloader extends AsyncTask<String, Void, Integer> {
    @Override
    protected Integer doInBackground(String... urls) {
        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {

            url = new URL(urls[0]);

            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);


            int data = reader.read();

            while(data != -1) {
                char current = (char)data;
                result += current;
                data = reader.read();

            }


            return Integer.parseInt(result);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) { super.onPostExecute(integer);}
}
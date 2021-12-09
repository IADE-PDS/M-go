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
        Log.e("REsult inside integer download", "before try");
        try {
            Log.e("REsult inside integer download", "1");
            url = new URL(urls[0]);
            Log.e("REsult inside integer download", "2");
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.e("REsult inside integer download", "3");

            InputStream in = urlConnection.getInputStream();
            Log.e("REsult inside integer download", "4");
            InputStreamReader reader = new InputStreamReader(in);
            Log.e("REsult inside integer download", "5");

            int data = reader.read();
            Log.e("REsult inside integer download", "6");
            while(data != -1) {
                Log.e("REsult inside integer download", "7");
                char current = (char)data;
                Log.e("REsult inside integer download", "8");
                result += current;
                Log.e("REsult inside integer download", "9");
                data = reader.read();
                Log.e("REsult inside integer download", "10");
            }

            Log.e("REsult inside integer download", "11");
            return Integer.parseInt(result);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) { super.onPostExecute(integer);}
}
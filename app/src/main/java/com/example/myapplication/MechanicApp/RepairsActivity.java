package com.example.myapplication.MechanicApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.Downloaders.JSONArrayDownloader;
import com.example.myapplication.PostPersons;
import com.example.myapplication.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RepairsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs);
        initializeMyListView();
    }

    private void initializeMyListView()
    {
        JSONArrayDownloader task = new JSONArrayDownloader();
        JSONArray objRepairs;
        try {

            objRepairs=task.execute("http://mechanic-on-the-go.herokuapp.com/api/repairs/repairTaken").get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objRepairs = null;
        }
        // 1 - Referenciar o componente do Design no Código
        ListView myList = (ListView) findViewById(R.id.myListView);

        // 2 - Criamos o tipo e o conjunto de objetos que vão ser listados
        ArrayList<String> myItems = new ArrayList<String>();
        myItems.add("Programação");
        myItems.add("Aplicações para Dispositivos Móveis");
        myItems.add("Computação Gráfica");
        myItems.add("Programação Orientada a Objetos");

        // 3 - Criar um adaptador para passar a ArrayList para a ListView
        ArrayAdapter<String> myListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myItems);

        // 4 - Associar o adaptador à ListView
        myList.setAdapter(myListAdapter);

        // 6 - Chamamos a função que nos implementa o listener
        createListViewClickItemEvent(myList, myItems);
    }

    private void createListViewClickItemEvent(ListView list, final ArrayList<String> items)
    {
        // 5 - Criamos um EventListener para lidar com o clique nos items da lista.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("INFO", "O nome da disciplina é: " + items.get(position));
            }
        });
    }
}
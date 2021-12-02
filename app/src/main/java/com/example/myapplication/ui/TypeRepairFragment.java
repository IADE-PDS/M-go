package com.example.myapplication.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.JSONArrayDownloader;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentTypeRepairBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class TypeRepairFragment extends Fragment {

    ListView myList;

    ArrayList<String> typeRepairId;
    ArrayList<String> typeRepairNames;

    private FragmentTypeRepairBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTypeRepairBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Inflate the layout for this fragment


        JSONArrayDownloader task = new JSONArrayDownloader();
        JSONArray objTypeRepair;
        try {
            objTypeRepair = task.execute("https://mechanic-on-the-go.herokuapp.com/api/typeRepair").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objTypeRepair = null;
        }

        JSONObject obj;
        typeRepairId = new ArrayList<>();
        typeRepairNames = new ArrayList<>();
        if(objTypeRepair != null) {
            for(int i = 0; i < objTypeRepair.length(); i++) {
                try {
                    obj = objTypeRepair.getJSONObject(i);
                    typeRepairId.add(obj.getString("id"));
                    typeRepairNames.add(obj.getString("typeRepairName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("1",typeRepairNames.toString());
        initializeMyLListView();
        return root;
    }
    private void initializeMyLListView(){
        myList=binding.TypeRepairListView;
        ArrayAdapter<String> myListAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,typeRepairNames);
        myList.setAdapter(myListAdapter);
    }

    public TypeRepairFragment() {
        // Required empty public constructor
    }

}
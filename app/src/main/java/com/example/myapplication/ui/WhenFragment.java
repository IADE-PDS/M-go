package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.PostPersons;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentWhenBinding;
import com.example.myapplication.ui.cars.HomeFragment;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class WhenFragment extends Fragment {
    private FragmentWhenBinding binding;
    private Button dateButton,urgentButton;
    public WhenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWhenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dateButton=binding.buttonDate;
        urgentButton=binding.buttonUrgent;

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_When_to_navigation_date_picker);
            }
        });

        urgentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String millisInString  = dateFormat.format(new Date());

                Log.e("data e horaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","" + millisInString);
                Map<String, String> postData1 = new HashMap<>();
                postData1.put("repairDate", "" + millisInString);

                PostPersons taks2 = new PostPersons(postData1);
                taks2.execute("https://mechanic-on-the-go.herokuapp.com/api/repairs/data/"+ HomeFragment.idRepair);
                Navigation.findNavController(view).navigate(R.id.action_When_to_maps_fragment);
                /*Intent intent=new Intent(getContext(),CustomPlacePickerActivity.class);
                startActivity(intent);*/
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

}
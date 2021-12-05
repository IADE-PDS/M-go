package com.example.myapplication.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDatePickerBinding;
import com.example.myapplication.databinding.FragmentWhenBinding;


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
                        .navigate(R.id.action_navigation_home_to_navigation_date_picker2);
            }
        });

        urgentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Inflate the layout for this fragment
        return root;
    }

}
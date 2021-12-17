package com.example.myapplication.MechanicApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentMechanicBinding;
import com.example.myapplication.databinding.FragmentOptionsBinding;


public class OptionsFragment extends Fragment {

    private FragmentOptionsBinding binding;

    public OptionsFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}
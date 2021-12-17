package com.example.myapplication.MechanicApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentMechanicBinding;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.example.myapplication.ui.profile.NotificationsViewModel;


public class MechanicFragment extends Fragment {


    private FragmentMechanicBinding binding;

    public MechanicFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMechanicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;

    }
}
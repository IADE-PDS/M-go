package com.example.myapplication.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.Downloaders.JSONObjDownloader;
import com.example.myapplication.R;
import com.example.myapplication.data.LoginDataSource;
import com.example.myapplication.databinding.FragmentNotificationsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class NotificationsFragment extends Fragment {

    private JSONObject objLogin;
    private String personName;
    TextView name,onGoing;


    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        name= binding.profileName;
        onGoing= binding.onGoging;

        onGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_notifications_to_navigation_on_going);
            }
        });
        getJson();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getJson(){
        JSONObjDownloader task = new JSONObjDownloader();
        String url = "https://mechanic-on-the-go.herokuapp.com/api/persons/"+ LoginDataSource.idint;
        try {
            objLogin = task.execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objLogin = null;
        }

        Log.e("Login", ""+objLogin);

        try {
            personName  = objLogin.getString("personName");
            name.setText(personName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
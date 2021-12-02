package com.example.myapplication;

import android.content.ClipData;
import android.widget.Spinner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ItemViewModel extends ViewModel {

    private List <Spinner> spinners = new ArrayList<>();


    public void addSpinner(Spinner s){
        spinners.add(s);
    }

    public Spinner getSpinner(){
      return spinners.get(0);
    }

    private final MutableLiveData<ClipData.Item> selectedItem = new MutableLiveData<ClipData.Item>();
    public void selectItem(ClipData.Item item) {
        selectedItem.setValue(item);
    }
    public LiveData<ClipData.Item> getSelectedItem() {
        return selectedItem;
    }
}

package com.john.graduate_project_mobile.Activities.Main.ui.showCar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

public class ShowCarViewModel extends ViewModel {
    private final MutableLiveData<JSONObject> data;

    public ShowCarViewModel() {
        this.data = new MutableLiveData<>();
        data.setValue(null);
    }

    public void setData(JSONObject object) {
        data.setValue(object);
    }
    public LiveData<JSONObject> getText() {
        return data;
    }
}

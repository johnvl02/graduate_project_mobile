package com.john.graduate_project_mobile.Activities.Main.ui.Requests.RequestRentYourCar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

public class RequestRentMyCarViewModel extends ViewModel {
    private final MutableLiveData<JSONObject> data;

    public RequestRentMyCarViewModel() {
        data = new MutableLiveData<>();
        data.setValue(null);
    }

    public LiveData<JSONObject> getData() {
        return data;
    }

    public void setData(JSONObject object) {
        data.setValue(object);
    }

}
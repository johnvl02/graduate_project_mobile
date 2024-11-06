package com.john.graduate_project_mobile.Activities.Main.ui.Requests.RequestRentACar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

public class RequestForViewModel extends ViewModel {
    private final MutableLiveData<JSONObject> data;

    public RequestForViewModel() {
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

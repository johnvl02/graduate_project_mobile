package com.john.graduate_project_mobile.Activities.Main.ui.EditCar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditCarViewModel extends ViewModel {

    private final MutableLiveData<String> license;

    public EditCarViewModel() {
        license = new MutableLiveData<>();
        license.setValue(null);
    }

    public LiveData<String> getData() {
        return license;
    }

    public void setData(String license) {
        this.license.setValue(license);
    }

}